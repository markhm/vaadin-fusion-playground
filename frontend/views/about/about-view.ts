import { showNotification } from '@vaadin/flow-frontend/a-notification';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement} from 'lit-element';

@customElement('about-view')
export class AboutView extends LitElement {
  name: string = '';

  static get styles() {
    return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
  }

  render() {
    return html`
<!--      <style>-->
<!--        about-view {-->
<!--          display: flex;-->
<!--          flex-direction: column;-->
<!--          height: 100%;-->
<!--          align-items: center;-->
<!--          justify-content: center;-->
<!--        }-->
<!--      </style>-->
      <div>
      
        <h3>About Survey world</h3>
        <div>This App was made with <a href="https://vaadin.com/fusion">Vaadin Fusion</a>, a framework that helps to rapidly deliver beautiful reactive client-side web apps with a Java backend. It is part of the open-source <a href="https://vaadin.com/">Vaadin</a> platform. This App contains a few <a href="fusion-examples">Fusion examples</a>.</a></div> 
        <br/>
        <img src="https://upload.wikimedia.org/wikipedia/commons/e/e0/Vaadin-logo.svg" height="80"/>
        <br/>
        <br/>
        <div>For authentication and authorization, this App uses <a href="https://www.okta.com/">okta</a>.</div>
        <br/>
        <img src="https://upload.wikimedia.org/wikipedia/commons/5/5c/Okta_logo.svg" height="60"/>
        <br/><br/>
        <div>Note that by using this app, you agree to our <a href="terms-and-conditions-view.ts">Terms and Conditions</a>.</div>
      </div>

    `;
  }
  nameChanged(e: CustomEvent) {
    this.name = e.detail.value;
  }

  sayHello() {
    showNotification('Hello ' + this.name);
  }
}
