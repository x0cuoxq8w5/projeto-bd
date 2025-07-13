/** @type {import('tailwindcss').Config} */

module.exports = {
  content: ["../resources/templates/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
    {
      malocaBlue: {
        "primary": "#37474F",
        "secondary": "#9bcae0",
        "accent": "#5ca4ed",
        "neutral": "#21272c",
        "base-100": "#ECEFF1",
      }
    },
    ],
  },
}

