import { unsafeCSS, customElement, html, LitElement, PropertyValues, query } from 'lit-element';

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
import UserModel from '../../generated/fusion/playground/domain/UserModel';
import { Binder, field } from '@vaadin/form';

import { CSSModule } from '@vaadin/flow-frontend/css-utils';
import styles from './create-account-view.css';

@customElement('create-account-view')
export class CreateAccountViewView extends LitElement {

  @query('#countryCode')
  private countryCode: any;

  static get styles() {
    return [CSSModule('lumo-typography'), unsafeCSS(styles)];
  }

  private binder = new Binder(this, UserModel);

  protected firstUpdated(_changedProperties: PropertyValues) {
    super.firstUpdated(_changedProperties);

    this.countryCode.items = ['+354', '+91', '+62', '+98', '+964', '+353', '+44', '+972', '+39', '+225'];
  }

  render() {
    return html`
      <h3>Create account (not implemented yet)</h3>
      <vaadin-form-layout style="width: 100%;">
        <vaadin-text-field label="First name" ...="${field(this.binder.model.firstName)}"></vaadin-text-field>
        <vaadin-text-field label="Last name" ...="${field(this.binder.model.lastName)}"></vaadin-text-field>
        <vaadin-text-field label="Username" ...="${field(this.binder.model.username)}"></vaadin-text-field>
        <vaadin-email-field label="Email address" ...="${field(this.binder.model.emailAddress)}"></vaadin-email-field>
        <vaadin-password-field label="Password" ...="${field(this.binder.model.passwordHash)}"></vaadin-password-field>
        <vaadin-text-field label="Question pointer" ...="${field(this.binder.model.questionPointer)}"></vaadin-text-field>
      </vaadin-form-layout>
      <vaadin-horizontal-layout class="button-layout" theme="spacing">
        <vaadin-button theme="primary" @click="${this.save}">
          Save
        </vaadin-button>
        <vaadin-button @click="${this.clearForm}">
          Cancel
        </vaadin-button>
      </vaadin-horizontal-layout>
    `;
  }

  private async save() {
    try {
      await this.binder.submitTo(UserEndpoint.update);
      this.clearForm();
      showNotification('Person details stored.', { position: 'bottom-start' });
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
