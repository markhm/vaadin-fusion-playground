import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import {css, customElement, html, LitElement} from 'lit-element';

@customElement('terms-and-conditions-view')
export class TermsAndConditionsView extends LitElement {
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
      <div>
        <h3>Terms and Conditions</h3>
        <div><i>To be added</i></div>
        
      </div>

    `;
  }
}
