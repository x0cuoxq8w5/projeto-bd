document.addEventListener('DOMContentLoaded', function () {
  // Elementos do DOM
  const tipoQuartoSelect = document.getElementById('tipoQuarto');
  const dataInicioInput = document.getElementById('dataInicio');
  const dataFinalInput = document.getElementById('dataFinal');
  const placeholderSection = document.getElementById('placeholder-section');
  const quartosGrid = document.getElementById('quartos-grid');
  const quartosContainer = document.getElementById('quartos-container');
  const quartoSelecionado = document.getElementById('quarto-selecionado');
  const numeroSelecionado = document.getElementById('numero-selecionado');
  const detalhesQuarto = document.getElementById('detalhes-quarto');
  const quartoIdInput = document.getElementById('quartoId');
  const cpfInput = document.getElementById('cpf');
  const cpfSpinner = document.getElementById('cpf-spinner');
  const cpfMessage = document.getElementById('cpf-message');
  const nomeInput = document.getElementById('nome');
  const dataNascimentoInput = document.getElementById('dataNascimento');
  const submitButton = document.querySelector('button[type="submit"]');



  let quartoAtualSelecionado = null;
  let todosOsQuartosDisponiveis = [];


  
  async function fetchAndRenderQuartos() {
    const dataInicio = dataInicioInput.value;
    const dataFim = dataFinalInput.value;
    const tipoQuarto = tipoQuartoSelect.value;

    if (!dataInicio || !dataFim) {
      placeholderSection.innerHTML = '<p class="text-center text-base-content/70">Selecione as datas de início e fim.</p>';
      placeholderSection.classList.remove('hidden');
      quartosGrid.classList.add('hidden');
      return;
    }

    // Mostrar loading
    placeholderSection.innerHTML = '<div class="flex justify-center items-center p-8"><span class="loading loading-lg loading-spinner text-primary"></span></div>';
    placeholderSection.classList.remove('hidden');
    quartosGrid.classList.add('hidden');
    resetSelecaoQuarto();

    try {
      const response = await fetch(`/api/reservas/quartos-disponiveis?dataInicio=${dataInicio}&dataFim=${dataFim}`);
      if (!response.ok) {
        throw new Error('Erro ao buscar quartos.');
      }
      todosOsQuartosDisponiveis = await response.json();

      // Filtra e renderiza
      filtrarERenderizar(tipoQuarto);

    } catch (error) {
      console.error(error);
      placeholderSection.innerHTML = '<p class="text-center text-error">Não foi possível carregar os quartos. Tente novamente.</p>';
    }
  }

  function filtrarERenderizar(tipoQuarto) {
    let quartosFiltrados = todosOsQuartosDisponiveis;
    if (tipoQuarto) {
      quartosFiltrados = todosOsQuartosDisponiveis.filter(q => q.tipoQuarto === tipoQuarto);
    }

    if (quartosFiltrados.length > 0) {
      placeholderSection.classList.add('hidden');
      quartosGrid.classList.remove('hidden');
      renderizarQuartos(quartosFiltrados);
    } else {
      placeholderSection.innerHTML = '<p class="text-center text-base-content/70">Nenhum quarto disponível para este período e tipo.</p>';
      placeholderSection.classList.remove('hidden');
      quartosGrid.classList.add('hidden');
    }
  }

  function renderizarQuartos(quartos) {
    quartosContainer.innerHTML = '';
    quartos.forEach(quarto => {
      const quartoElement = document.createElement('div');
      quartoElement.className = 'relative w-16 h-16 border-2 rounded-lg flex items-center justify-center font-bold text-sm transition-all duration-200 cursor-pointer hover:border-base-300 hover:bg-primary/10 hover:scale-105 hover:shadow-lg bg-base-100 border-primary';
      quartoElement.innerHTML = `<span class="relative z-10 text-base-content">${quarto.numero}</span>`;
      quartoElement.dataset.quartoId = quarto.id;
      quartoElement.addEventListener('click', () => selecionarQuarto(quarto, quartoElement));
      quartosContainer.appendChild(quartoElement);
    });
  }

  function selecionarQuarto(quarto, elemento) {
    if (quartoAtualSelecionado) {
      quartoAtualSelecionado.classList.remove('border-base-300', 'bg-primary/20', 'shadow-lg');
      quartoAtualSelecionado.classList.add('border-primary');
    }
    elemento.classList.add('border-base-300', 'bg-primary/20', 'shadow-lg');
    elemento.classList.remove('border-primary');
    quartoAtualSelecionado = elemento;

    numeroSelecionado.textContent = quarto.numero;
    detalhesQuarto.textContent = `${quarto.numero} - ${getTipoQuartoNome(quarto.tipoQuarto)}`;
    quartoIdInput.value = quarto.id;
    quartoSelecionado.classList.remove('hidden');
  }

  function resetSelecaoQuarto() {
    quartoAtualSelecionado = null;
    quartoSelecionado.classList.add('hidden');
    quartoIdInput.value = '';
  }

  function getTipoQuartoNome(tipo) {
    const tipos = { SIMPLES: 'Simples', SUITE: 'Suíte', COBERTURA: 'Cobertura' };
    return tipos[tipo] || tipo;
  }

  async function fetchHospedePorCpf() {
    const cpf = cpfInput.value;
    if (cpf.length !== 14) { 
      resetHospedeFields();
      return;
    }

    cpfSpinner.classList.remove('hidden');
    cpfMessage.textContent = '';

    try {
      const response = await fetch(`/api/hospedes/${cpf}`);
      if (response.ok) {
        const hospede = await response.json();

        if (hospede.desativado) {
          // Hóspede encontrado, mas desativado
          nomeInput.value = hospede.nome;
          dataNascimentoInput.value = hospede.dataNascimento.substring(0, 10);
          nomeInput.readOnly = true;
          dataNascimentoInput.readOnly = true;
          cpfMessage.textContent = 'Hóspede desativado. Não é possível criar uma reserva.';
          cpfMessage.className = 'label-text-alt text-error';
          submitButton.disabled = true;
        } else {
          // Hóspede encontrado e ativo
          nomeInput.value = hospede.nome;
          dataNascimentoInput.value = hospede.dataNascimento.substring(0, 10);
          nomeInput.readOnly = true;
          dataNascimentoInput.readOnly = true;
          cpfMessage.textContent = 'Hóspede encontrado.';
          cpfMessage.className = 'label-text-alt text-success';
          submitButton.disabled = false;
        }
      } else {
        // Hóspede não encontrado
        resetHospedeFields(true);
        cpfMessage.textContent = 'Novo hóspede. Preencha os dados.';
        cpfMessage.className = 'label-text-alt text-info';
        submitButton.disabled = false;
      }
    } catch (error) {
      console.error('Erro ao buscar hóspede:', error);
      resetHospedeFields(true);
      cpfMessage.textContent = 'Erro ao buscar CPF. Tente novamente.';
      cpfMessage.className = 'label-text-alt text-error';
      submitButton.disabled = false;
    }
    finally {
        cpfSpinner.classList.add('hidden');
    }
  }

  function resetHospedeFields(enableFields = false) {
      if(enableFields){
          nomeInput.value = '';
          dataNascimentoInput.value = '';
          nomeInput.readOnly = false;
          dataNascimentoInput.readOnly = false;
      } else {
          nomeInput.readOnly = true;
          dataNascimentoInput.readOnly = true;
      }
      cpfMessage.textContent = '';
  }

  // Event Listeners
  dataInicioInput.addEventListener('change', fetchAndRenderQuartos);
  dataFinalInput.addEventListener('change', fetchAndRenderQuartos);
  tipoQuartoSelect.addEventListener('change', () => {
    resetSelecaoQuarto();
    if(!dataInicioInput.value || !dataFinalInput.value) return;
    
    filtrarERenderizar(tipoQuartoSelect.value);
  });
  cpfInput.addEventListener('blur', fetchHospedePorCpf);

  // Inicialização
  fetchAndRenderQuartos();
});

// CPF máscara
document.addEventListener('DOMContentLoaded', function () {
  const cpfInput = document.querySelector('input[name="cpf"]');
  if (cpfInput) {
    cpfInput.addEventListener('input', function (e) {
      let value = e.target.value.replace(/\D/g, '');
      value = value.replace(/(\d{3})(\d)/, '$1.$2');
      value = value.replace(/(\d{3})(\d)/, '$1.$2');
      value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
      e.target.value = value;
    });
  }
});
