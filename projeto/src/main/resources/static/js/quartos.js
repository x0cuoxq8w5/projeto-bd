document.addEventListener('DOMContentLoaded', () => {
  const quartos = document.querySelectorAll('.quarto-item');

  const statusClasses = {
    DISPONIVEL: 'border-success bg-success/20',
    OCUPADO: 'border-error bg-error/20',
    LIMPEZA: 'border-warning bg-warning/20',
    default: 'border-base-300 bg-base-100',
  };

  quartos.forEach(quarto => {
    const status = quarto.dataset.status;
    const classes = statusClasses[status] || statusClasses.default;
    quarto.classList.add(...classes.split(' '));
  });
});
