import { customElement, html, internalProperty, LitElement } from 'lit-element';
import { signIn } from '../../auth';
import '@vaadin/vaadin-login/vaadin-login-form';

@customElement('login-view')
export class LoginView extends LitElement {
    @internalProperty()
    private error = !!new URLSearchParams().get('error');

    render() {
        return html`
     <style>
       login-view {
         display: flex;
         flex-direction: column;
         height: 100%;
         align-items: center;
         justify-content: center;
       }
     </style>
     <vaadin-login-form 
       @login=${this.login} 
       ?error=${this.error}
       no-forgot-password
     ></vaadin-login-form>
<!--     <div><b>or</b></div>-->
<!--     <br/>-->
<!--     <facebook-login-view></facebook-login-view>-->
<!--     <div>Username: "testuser@test.com"</div>-->
<!--     <div>Password: "72Dy" combined with "ZRJn"</div>-->
     
     <div>Create an <a href="/create-account">account</a>.</div>
     <div>If you forgot your <a href="https://dev-8673725.okta.com/signin/forgot-password">password</a>.</div><br/>
     <div>This App uses <a href="https://www.okta.com/">okta</a> for identity management.</div>
     <br/>
     <img src="https://upload.wikimedia.org/wikipedia/commons/5/5c/Okta_logo.svg" height="50"/>
   `;
    }

    async login(e: CustomEvent) {
        try {
            await signIn(e.detail.username, e.detail.password);
        } catch (e) {
            this.error = true;
        }
    }

    // Render in light DOM for password managers
    protected createRenderRoot() {
        return this;
    }
}
