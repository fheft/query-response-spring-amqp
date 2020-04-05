// ELEMENTS -------------------------------------------------------------------

const selectLiByOnClick = fun =>
  document.querySelector(`li[onclick*='${fun}']`);

const TOGGLE = selectLiByOnClick("toggleDarkTheme");
const HOME = selectLiByOnClick("overview");
const LIVE = selectLiByOnClick("live");
const TOPOLOGY = selectLiByOnClick("topology");
const LOGGING = selectLiByOnClick("logging");
const SETTINGS = selectLiByOnClick("settings");

const toggleTheme = source => {
  if (($html = document.querySelector("html[data-theme]"))) {
    clearDarkTheme($html, TOGGLE);
  } else {
    setDarkTheme(document.querySelector("html"), TOGGLE);
  }
};

const clearDarkTheme = ($html, $toggle) => {
  $html.removeAttribute("data-theme");
  $toggle.firstChild && $toggle.removeChild($toggle.firstChild);
  $toggle.appendChild(MOON_ICON);
  window.localStorage.removeItem("theme");
};

const setDarkTheme = ($html, $toggle) => {
  $html.setAttribute("data-theme", "dark");
  $toggle.firstChild && $toggle.removeChild($toggle.firstChild);
  $toggle.appendChild(SUN_ICON);
  window.localStorage.setItem("theme", "dark");
};

// HTML/DOM UTILS -------------------------------------------------------------

const h2e = html => {
  var template = document.createElement("template");
  html = html.trim();
  template.innerHTML = html;
  return template.content.firstChild;
};

// SVG ICONS ------------------------------------------------------------------

const SUN_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-sun" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
<path stroke="none" d="M0 0h24v24H0z"/>
<circle cx="12" cy="12" r="4" />
<path d="M3 12h1M12 3v1M20 12h1M12 20v1M5.6 5.6l.7 .7M18.4 5.6l-.7 .7M17.7 17.7l.7 .7M6.3 17.7l-.7 .7" />
</svg>`);

const MOON_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-moon" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
<path stroke="none" d="M0 0h24v24H0z"/>
<path d="M16.2 4a9.03 9.03 0 1 0 3.9 12a6.5 6.5 0 1 1 -3.9 -12" />
</svg>`);

const HOME_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-home-2" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
<path stroke="none" d="M0 0h24v24H0z"/>
<polyline points="5 12 3 12 12 3 21 12 19 12" />
<path d="M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2 -2v-7" />
<rect x="10" y="12" width="4" height="4" />
</svg>`);

const LIVE_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-activity" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
  <path stroke="none" d="M0 0h24v24H0z"/>
  <polyline points="21 12 17 12 14 20 10 4 7 12 3 12" />
</svg>`);

const TOPOLOGY_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-route" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
  <path stroke="none" d="M0 0h24v24H0z"/>
  <circle cx="6" cy="19" r="2" />
  <circle cx="18" cy="5" r="2" />
  <path d="M12 19h4.5a3.5 3.5 0 0 0 0 -7h-8a3.5 3.5 0 0 1 0 -7h3.5" />
</svg>`);

const LOGGING_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-file-text" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
  <path stroke="none" d="M0 0h24v24H0z"/>
  <polyline points="14 3 14 8 19 8" />
  <path d="M17 21H7a2 2 0 0 1 -2 -2V5a2 2 0 0 1 2 -2h7l5 5v11a2 2 0 0 1 -2 2z" />
  <line x1="9" y1="9" x2="10" y2="9" />
  <line x1="9" y1="13" x2="15" y2="13" />
  <line x1="9" y1="17" x2="15" y2="17" />
</svg>`);

const SETTINGS_ICON = h2e(`
<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-settings" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
  <path stroke="none" d="M0 0h24v24H0z"/>
  <path d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 0 0 2.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 0 0 1.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 0 0 -1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 0 0 -2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 0 0 -2.573 -1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 0 0 -1.065 -2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 0 0 1.066 -2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
  <circle cx="12" cy="12" r="3" />
</svg>`);

// MAIN -----------------------------------------------------------------------

const addNavigation = (el, icon, title) => {
  el.appendChild(icon);
  el.appendChild(h2e(`<h2>${title}</h2>`));
};

addNavigation(HOME, HOME_ICON, "Overview");
addNavigation(LIVE, LIVE_ICON, "Live Activity");
addNavigation(TOPOLOGY, TOPOLOGY_ICON, "Q/R Topology");
addNavigation(LOGGING, LOGGING_ICON, "Logging");
addNavigation(SETTINGS, SETTINGS_ICON, "Settings");

// DARK/LIGHT MODE ------------------------------------------------------------

const HTML = document.querySelector("html");

if (window.localStorage.getItem("theme")) {
  setDarkTheme(HTML, TOGGLE);
} else {
  clearDarkTheme(HTML, TOGGLE);
}

window.matchMedia("(prefers-color-scheme: dark)").addListener(evt => {
  if (evt.matches) {
    setDarkTheme(HTML, TOGGLE);
  } else {
    clearDarkTheme(HTML, TOGGLE);
  }
});

// EXPORTS --------------------------------------------------------------------

window.toggleDarkTheme = toggleTheme;