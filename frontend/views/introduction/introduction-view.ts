import {css, customElement, html, LitElement, property} from 'lit-element';

// security
import { isAuthenticated } from '../../auth';

@customElement('introduction-view')
export class IntroductionView extends LitElement {

  @property( {type: Boolean }) isAuthenticated = false;

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
        <h3>Welcome to the Vaadin Fusion Playground!</h3>
        <div></div>
        <div>Here we will build various Vaadin Fusion examples.</div>
        <br/>

        ${!this.isAuthenticated ? html`
          <div><vaadin-button @click="${()=>this.clickLogin()}">Login</vaadin-button> using the test credentials or <br/>
            
            <vaadin-button disabled @click="${()=>this.clickLogin()}">Create an account</vaadin-button> 
                if needed (not yet implemented).</div>
        ` : html``}
        
        ${this.isAuthenticated ? html`
          <div>See <a href="questions">Questions</a> for multiple choice questions.</div>
        ` : html``}
        
    `;
  }

  async connectedCallback() {
    super.connectedCallback();

    this.isAuthenticated = await isAuthenticated();
  }

  private clickLogin() {
    // open Spring login form
    window.location.replace('login');
  }

}
