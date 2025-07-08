/** @type {import('tailwindcss').Config} */

module.exports = {
  content: ["../resources/templates/**/*.{html,js}"],
  theme: {
    extend: {},
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: ["luxury", "forest"],
  },
}

