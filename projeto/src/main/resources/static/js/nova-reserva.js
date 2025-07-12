document.addEventListener('DOMContentLoaded', function () {
  const tipoQuartoSelect = document.getElementById('tipoQuarto');
  const placeholderSection = document.getElementById('placeholder-section');
  const quartosGrid = document.getElementById('quartos-grid');
  const quartosContainer = document.getElementById('quartos-container');
  const quartoSelecionado = document.getElementById('quarto-selecionado');
  const numeroSelecionado = document.getElementById('numero-selecionado');
  const detalhesQuarto = document.getElementById('detalhes-quarto');
  const quartoIdInput = document.getElementById('quartoId');

  let quartoAtualSelecionado = null;

  // Função para filtrar quartos por tipo
  function filtrarQuartos(tipoQuarto) {
    if (!tipoQuarto) return [];

    return window.quartosData.filter(
      (quarto) => quarto.tipoQuarto === tipoQuarto
    );
  }

  // Função para renderizar os quartos
  function renderizarQuartos(quartos) {
    quartosContainer.innerHTML = '';

    quartos.forEach((quarto) => {
      const quartoElement = document.createElement('div');

      // Base classes for all states
      quartoElement.className = `
                relative w-16 h-16 border-2 rounded-lg flex items-center justify-center
                font-bold text-sm transition-all duration-200
            `;

      let overlayHtml = '';

      if (quarto.ocupado) {
        quartoElement.classList.add(
          'bg-base-300',
          'border-base-300',
          'cursor-not-allowed'
        );
        // Overlay to indicate it's occupied
        overlayHtml =
          '<div class="absolute inset-0 bg-red-500/20 rounded"></div>';
      } else if (quarto.marcadoParaLimpeza) {
        quartoElement.classList.add(
          'bg-yellow-300/30',
          'border-base-300',
          'cursor-not-allowed'
        );
      } else {
        // Available
        quartoElement.classList.add(
          'bg-base-100',
          'border-base-300',
          'cursor-pointer',
          'hover:border-primary',
          'hover:bg-primary/10',
          'hover:scale-105',
          'hover:shadow-lg'
        );
      }

      quartoElement.innerHTML = `
                <span class="relative z-10 text-base-content">${quarto.numero}</span>
                ${overlayHtml}
            `;

      quartoElement.dataset.quartoId = quarto.id;
      quartoElement.dataset.numero = quarto.numero;
      quartoElement.dataset.ocupado = quarto.ocupado;
      quartoElement.dataset.marcadoParaLimpeza = quarto.marcadoParaLimpeza;

      if (!quarto.ocupado && !quarto.marcadoParaLimpeza) {
        quartoElement.addEventListener('click', function () {
          selecionarQuarto(quarto);
        });
      }

      quartosContainer.appendChild(quartoElement);
    });
  }

  // Função para selecionar um quarto
  function selecionarQuarto(quarto) {
    // Remove seleção anterior
    if (quartoAtualSelecionado) {
      quartoAtualSelecionado.classList.remove(
        'border-primary',
        'bg-primary/20',
        'shadow-lg'
      );
      quartoAtualSelecionado.classList.add('border-base-300');
    }

    // Seleciona novo quarto
    const quartoElement = document.querySelector(
      `[data-quarto-id="${quarto.id}"]`
    );
    quartoElement.classList.remove('border-base-300');
    quartoElement.classList.add('border-primary', 'bg-primary/20', 'shadow-lg');

    quartoAtualSelecionado = quartoElement;

    // Atualiza campos
    numeroSelecionado.textContent = quarto.numero;
    detalhesQuarto.textContent = `${quarto.numero} - ${getTipoQuartoNome(
      quarto.tipoQuarto
    )}`;
    quartoIdInput.value = quarto.id;

    // Mostra seção de quarto selecionado
    quartoSelecionado.classList.remove('hidden');
  }

  // Função para obter nome do tipo de quarto
  function getTipoQuartoNome(tipo) {
    const tipos = {
      SIMPLES: 'Quarto Simples',
      SUITE: 'Suíte',
      COBERTURA: 'Cobertura',
    };
    return tipos[tipo] || tipo;
  }

  // Event listener para mudança de tipo de quarto
  tipoQuartoSelect.addEventListener('change', function () {
    const tipoSelecionado = this.value;

    // Reset da seleção anterior
    quartoAtualSelecionado = null;
    quartoSelecionado.classList.add('hidden');
    quartoIdInput.value = '';

    if (tipoSelecionado) {
      const quartosFiltrados = filtrarQuartos(tipoSelecionado);

      placeholderSection.classList.add('hidden');
      quartosGrid.classList.remove('hidden');

      renderizarQuartos(quartosFiltrados);
    } else {
      placeholderSection.classList.remove('hidden');
      quartosGrid.classList.add('hidden');
    }
  });

  // Inicialização
  if (tipoQuartoSelect.value) {
    tipoQuartoSelect.dispatchEvent(new Event('change'));
  }
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
