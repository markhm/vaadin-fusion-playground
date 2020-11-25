import { css, customElement, html, LitElement, property } from 'lit-element';
import '@vaadin/vaadin-app-layout/theme/lumo/vaadin-app-layout';
// @ts-ignore
import { AppLayoutElement } from '@vaadin/vaadin-app-layout/src/vaadin-app-layout';
import '@vaadin/vaadin-app-layout/vaadin-drawer-toggle';
import '@vaadin/vaadin-tabs/theme/lumo/vaadin-tab';
import '@vaadin/vaadin-tabs/theme/lumo/vaadin-tabs';
import { CSSModule } from '@vaadin/flow-frontend/css-utils';
import { router } from '../../index';

import { setCookie, getCookie } from "../../utils/security";
import { isAuthenticated } from "../../utils/security";

// Rename to something more appropriate.
interface MenuTab {
  route: string;
  name: string;
}

const COOKIE_DOMAIN = 'https://vaadin-fusion-playground.herokuapp.com';

@customElement('main-view')
export class MainView extends LitElement {
  @property({ type: Object }) location = router.location;

  @property({ type: Array }) menuTabs : MenuTab[] = [];

  @property({ type: String }) projectName = '';

  static get styles() {
    return [
      CSSModule('lumo-typography'),
      CSSModule('lumo-color'),
      CSSModule('app-layout'),
      css`
        :host {
          display: block;
          height: 100%;
        }

        header {
          align-items: center;
          box-shadow: var(--lumo-box-shadow-s);
          display: flex;
          height: var(--lumo-size-xl);
          width: 100%;
        }

        header h1 {
          font-size: var(--lumo-font-size-l);
          margin: 0;
        }

        header img {
          border-radius: 50%;
          height: var(--lumo-size-s);
          margin-left: auto;
          margin-right: var(--lumo-space-m);
          overflow: hidden;
          background-color: var(--lumo-contrast);
        }

        vaadin-app-layout[dir='rtl'] header img {
          margin-left: var(--lumo-space-m);
          margin-right: auto;
        }

        #logo {
          align-items: center;
          box-sizing: border-box;
          display: flex;
          padding: var(--lumo-space-s) var(--lumo-space-m);
        }

        #logo img {
          height: calc(var(--lumo-size-l) * 0.7);
        }

        #logo span {
          font-size: var(--lumo-font-size-xl);
          font-weight: 600;
          margin: 0 var(--lumo-space-s);
        }

        vaadin-tab {
          font-size: var(--lumo-font-size-s);
          height: var(--lumo-size-l);
          font-weight: 600;
          color: var(--lumo-body-text-color);
        }

        vaadin-tab:hover {
          background-color: var(--lumo-contrast-5pct);
          text-decoration: none;
        }

        vaadin-tab[selected] {
          background-color: var(--lumo-primary-color-10pct);
          color: var(--lumo-primary-text-color);
        }

        hr {
          margin: 0;
        }
      `,
    ];
  }

  render() {
    return html`
      <vaadin-app-layout primary-section="drawer">
        <header slot="navbar" theme="dark">
          <vaadin-drawer-toggle></vaadin-drawer-toggle>
          <h1>${this.getSelectedTabName(this.menuTabs)}</h1>
          <div style=""></div>
          <div><a href="/login"><img src="images/user.svg" alt="Avatar" /></a></div>
        </header>

        <div slot="drawer">
          <div id="logo">
            <img src="images/question-mark.jpg" alt="${this.projectName} logo" />
            <span>${this.projectName}</span>
          </div>
          <hr />
          <vaadin-tabs orientation="vertical" theme="minimal" id="tabs" .selected="${this.getIndexOfSelectedTab()}">
            ${this.menuTabs.map(
              (menuTab) => html`
                <vaadin-tab>
                  <a href="${router.urlForPath(menuTab.route)}" tabindex="-1">${menuTab.name}</a>
                </vaadin-tab>
              `
            )}
             <vaadin-tab>...</vaadin-tab>
             <vaadin-tab> <a href="#" @click="${this.login}">Login</a> </vaadin-tab>
             <vaadin-tab> <a href="#" @click="${this.logout}">Logout</a> </vaadin-tab>
             <vaadin-tab>--------</vaadin-tab>
             <vaadin-tab> <a href="#" @click="${this.cookieStatus}">cookieStatus()</a> </vaadin-tab>
             <vaadin-tab> <a href="#" @click="${this.simulateLogin}">simulateLogin()</a> </vaadin-tab>
             <vaadin-tab> <a href="#" @click="${this.simulateLogout}">simulateLogout()</a> </vaadin-tab>

          </vaadin-tabs>
        </div>
        <slot></slot>
      </vaadin-app-layout>
    `;
  }

  private _routerLocationChanged() {
    // @ts-ignore
    AppLayoutElement.dispatchCloseOverlayDrawerEvent();
  }

  connectedCallback() {
    super.connectedCallback();
    window.addEventListener('vaadin-router-location-changed', this._routerLocationChanged);
    this.projectName = 'Playground';

    if (isAuthenticated(COOKIE_DOMAIN)) {
      this.menuTabs = [
        {route: '', name: 'Introduction'},
        {route: 'account/create', name: 'Create account'},
        {route: 'test/hello-world', name: 'Hello World'},
        {route: 'events', name: 'Events'},
        {route: 'question', name: 'Question'},
      ];
    }
    else {
      this.menuTabs = [
        {route: '', name: 'Introduction'},
        // {route: 'account/create', name: 'Create account'},
        // {route: 'test/hello-world', name: 'Hello World'},
        // {route: 'events', name: 'Events'},
        // {route: 'questions', name: 'Questions'},
      ];
    }

  }

  disconnectedCallback() {
    super.disconnectedCallback();
    window.removeEventListener('vaadin-router-location-changed', this._routerLocationChanged);
  }

  private isCurrentLocation(route: string): boolean {
    return router.urlForPath(route) === this.location.getUrl();
  }

  private getIndexOfSelectedTab(): number {
    const index = this.menuTabs.findIndex((menuTab) => this.isCurrentLocation(menuTab.route));

    // Select first tab if there is no tab for home in the menu
    if (index === -1 && this.isCurrentLocation('')) {
      return 0;
    }

    return index;
  }

  private getSelectedTabName(menuTabs: MenuTab[]): string {
    const currentTab = menuTabs.find((menuTab) => this.isCurrentLocation(menuTab.route));
    let tabName = '';
    if (currentTab) {
      tabName = currentTab.name;
    } else {
      tabName = 'Introduction';
    }
    return tabName;
  }

  private cookieStatus() {
    var userEmail = getCookie(COOKIE_DOMAIN);
    console.log('getCookie is: ' + userEmail);
  }

  private simulateLogin() {
    setCookie(COOKIE_DOMAIN,'testuser@gmail.com',30);
    window.location.reload();
  }

  private simulateLogout() {
    setCookie(COOKIE_DOMAIN, '',30);
  }

  private login() {
    // open Spring login form
    window.location.replace('login');
  }

  private async logout() {
    // call via ajax to the Spring logout form
    await fetch('logout');
    // clean the ui
    window.location.reload();
  }
}
