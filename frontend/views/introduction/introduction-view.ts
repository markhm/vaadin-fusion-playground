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
        <div>Here we will build various Vaadin Fusion examples and constructs and integrate them in a production-ready solution, to explore how close a Fusion-based PWA can get to a native (iOS) experience.</div>
        <br/>
        <div>Our first goal is to build a Survey system, which will include the following features:</div>
        <ul>
          <li>Login via Okta and the ability to see your user profile.</li>
          <li>Select a survey from a list and take it by answering multiple choice questions.</li>
          <li>See the survey results and the achievement reflected in your user profile.</li>
        </ul>

        ${!this.isAuthenticated ? html`
          <div><vaadin-button @click="${()=>this.clickLogin()}">Login</vaadin-button> using the test credentials or <br/>
            
            <vaadin-button disabled @click="${()=>this.clickLogin()}">Create an account</vaadin-button> 
                if needed (not yet implemented).</div>
        ` : html``}
        
        ${this.isAuthenticated ? html`
          <div>See <a href="surveys">surveys</a> to select your first survey.</div>
        ` : html``}
        
        <br/>
        Feel welcome to join the fun. The source code and more information is found in <a href="https://github.com/markhm/vaadin-fusion-playground">Github</a>.
        
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
