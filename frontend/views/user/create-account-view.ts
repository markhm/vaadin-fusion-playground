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
import UserModel from '../../generated/fusion/playground/data/entity/UserModel';
import { Binder, field } from '@vaadin/form';

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

  private binder = new Binder(this, UserModel);

  protected firstUpdated(_changedProperties: PropertyValues) {
    super.firstUpdated(_changedProperties);

  }

  render() {
    return html`        
      <div>Welcome to the Fusion Playground Survey world...!</div></br>
      <div>This is where you complete surveys and get recognition for it. </div><br/>
      <div>Fill in the form to request an account.</div>
      
      <vaadin-form-layout style="width: 100%;">
        <vaadin-text-field label="Username (nice and anonymous 😊) " ...="${field(this.binder.model.username)}"></vaadin-text-field>
        <vaadin-date-picker label="Date of birth (you should be over 13 to join)" ...="${field(this.binder.model.dateOfBirth)}"></vaadin-date-picker>
        <vaadin-email-field label="Email" ...="${field(this.binder.model.emailAddress)}"></vaadin-email-field>
      </vaadin-form-layout>
      <div>You will receive an email where you are able to set your password.</div>
      </br>
      
      <vaadin-horizontal-layout class="button-layout" theme="spacing">
        <vaadin-button theme="primary" ?disabled=${this.binder.invalid || this.binder.submitting} @click="${this.save}">Submit</vaadin-button>
        <vaadin-button @click="${this.clearForm}">Clear</vaadin-button>
      </vaadin-horizontal-layout>
    `;
  }

  private async save() {
    try {
      await this.binder.submitTo(UserEndpoint.createUser);
      this.clearForm();
        showNotification('Check your mail to confirm your account.', { position: 'bottom-start' });
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
