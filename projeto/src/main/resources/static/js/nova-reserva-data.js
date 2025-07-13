document.addEventListener('DOMContentLoaded', function () {
    const dataInicio = document.getElementById('dataInicio');
    const dataFinal = document.getElementById('dataFinal');
    const infoPeriodo = document.getElementById('info-periodo');
    const duracaoEstadia = document.getElementById('duracao-estadia');
    const inicioFormatado = document.getElementById('inicio-formatado');
    const finalFormatado = document.getElementById('final-formatado');

    // Define data mínima como hoje
    const hoje = new Date().toISOString().split('T')[0];
    dataInicio.min = hoje;
    dataFinal.min = hoje;

    function formatarData(dataString) {
        // Corrige o problema de fuso horário ao fazer o parse da data YYYY-MM-DD
        const [ano, mes, dia] = dataString.split('-').map(Number);
        const data = new Date(ano, mes - 1, dia);

        const options = {
            day: 'numeric',
            month: 'long',
            year: 'numeric',
        };
        return data.toLocaleDateString('pt-BR', options);
    }

    function calcularDiferenca(inicio, fim) {
        const dataInicial = new Date(inicio);
        const dataFinal = new Date(fim);
        const diferencaMS = dataFinal - dataInicial;
        const dias = Math.ceil(diferencaMS / (1000 * 60 * 60 * 24));
        return dias;
    }

    function atualizarInfoPeriodo() {
        if (dataInicio.value && dataFinal.value) {
            const inicio = dataInicio.value;
            const fim = dataFinal.value;

            if (new Date(fim) > new Date(inicio)) {
                const dias = calcularDiferenca(inicio, fim);

                duracaoEstadia.textContent = `${dias} ${
                    dias === 1 ? 'dia' : 'dias'
                }`;
                inicioFormatado.textContent = formatarData(inicio);
                finalFormatado.textContent = formatarData(fim);

                infoPeriodo.style.display = 'block';
            } else {
                infoPeriodo.style.display = 'none';
            }
        } else {
            infoPeriodo.style.display = 'none';
        }
    }

    // Validação quando a data de início muda
    dataInicio.addEventListener('change', function () {
        if (this.value) {
            // Define data mínima para data final como um dia após a data de início
            const dataInicioObj = new Date(this.value);
            dataInicioObj.setDate(dataInicioObj.getDate() + 1);
            dataFinal.min = dataInicioObj.toISOString().split('T')[0];

            // Se a data final já estiver definida e for menor que a nova data mínima, limpa
            if (
                dataFinal.value &&
                new Date(dataFinal.value) <= new Date(this.value)
            ) {
                dataFinal.value = '';
            }
        }
        atualizarInfoPeriodo();
    });

    // Validação quando a data final muda
    dataFinal.addEventListener('change', function () {
        if (this.value && dataInicio.value) {
            if (new Date(this.value) <= new Date(dataInicio.value)) {
                alert('A data final deve ser posterior à data de início.');
                this.value = '';
                return;
            }
        }
        atualizarInfoPeriodo();
    });
});