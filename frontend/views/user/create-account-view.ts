import {customElement, html, LitElement, PropertyValues, css} from 'lit-element';

import '@vaadin/vaadin-button/vaadin-button';
import '@vaadin/vaadin-custom-field/vaadin-custom-field';
import '@vaadin/vaadin-combo-box/vaadin-combo-box';
import '@vaadin/vaadin-date-picker/vaadin-date-picker';
import '@vaadin/vaadin-form-layout/vaadin-form-layout';
import '@vaadin/vaadin-item/vaadin-item';
import '@vaadin/vaadin-notification/vaadin-notification';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-radio-button/vaadin-radio-button';
import '@vaadin/vaadin-radio-button/vaadin-radio-group';
import '@vaadin/vaadin-text-field/vaadin-email-field';
import '@vaadin/vaadin-text-field/vaadin-number-field';
import '@vaadin/vaadin-text-field/vaadin-text-field';

import { showNotification } from '@vaadin/flow-frontend/a-notification';

import { EndpointError } from '@vaadin/flow-frontend/Connect';
import * as UserEndpoint from '../../generated/UserEndpoint';
import UserVOModel from '../../generated/fusion/playground/views/user/UserVOModel';
import { Binder, field } from '@vaadin/form';
import {Router} from "@vaadin/router";

@customElement('create-account-view')
export class CreateAccountView extends LitElement {

  static get styles() {
    return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
  }

  private binder = new Binder(this, UserVOModel);

  protected firstUpdated(_changedProperties: PropertyValues) {
    super.firstUpdated(_changedProperties);

  }

  render() {
    return html`        
      <div>Welcome to the Fusion Playground Survey world...!</div></br>
      <div>This is where you complete surveys and get recognition for it. </div><br/>
      <div>Fill in the form to create an account.</div>
      
      <vaadin-form-layout style="width: 50%;">
        <vaadin-text-field label="Username (anonymous, but your friends would recognise)"
                           ...="${field(this.binder.model.username)}"></vaadin-text-field>
        <vaadin-date-picker label="Date of birth (you should be over 13 to join)" 
                            ...="${field(this.binder.model.dateOfBirth)}"></vaadin-date-picker>
        <vaadin-email-field label="Email" 
                            ...="${field(this.binder.model.emailAddress)}"></vaadin-email-field>
        <vaadin-password-field label="Password (note: there is no password reset function yet)"
                            ...="${field(this.binder.model.password)}"></vaadin-password-field>
      </vaadin-form-layout>
      </br>
      <div>Note: Vaadin Fusion Playground uses Okta, a third party provider that will ask you to confirm your email and set a password. After doing so, you can log in.</div><br/>
      
      <vaadin-horizontal-layout class="button-layout" theme="spacing">
        <vaadin-button theme="primary" ?disabled=${this.binder.invalid || this.binder.submitting} @click="${this.save}">Submit</vaadin-button>
        <vaadin-button @click="${this.clearForm}">Clear</vaadin-button>
        ${this.binder.submitting ? html`
            <span class="label">submitting</span>
            <div class="spinner"></div>` : html``}
      </vaadin-horizontal-layout>
      
      </br>
      <div>After clicking Submit, you will will be routed to the Login page.</div>
    `;
  }

  private async save() {
    try {
      await this.binder.submitTo(UserEndpoint.createUser);
      this.clearForm();
        showNotification('Your user was created. You can login now.', { position: 'bottom-start' });
        Router.go('/login');

    } catch (error) {
      if (error instanceof EndpointError) {
        showNotification('Server error. ' + error.message, { position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private clearForm() {
    this.binder.clear();
  }
}
