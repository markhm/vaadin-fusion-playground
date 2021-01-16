import {css, html, internalProperty, LitElement} from 'lit-element';

import { showNotification } from '@vaadin/flow-frontend/a-notification';
import { EndpointError } from '@vaadin/flow-frontend/Connect';
import { Binder, field } from '@vaadin/form';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';

import * as SurveyEndpoint from "../../../generated/SurveyEndpoint";
import SurveyStepModel from "../../../generated/fusion/playground/data/entity/SurveyStepModel";

export class AddQuestion extends LitElement {
    name: string = '';

    @internalProperty()
    surveyId! : string;

    private binder = new Binder(this, SurveyStepModel);

    static get styles() {
        return css`
      :host {
        display: block;
        padding: 0em;
      }
    `;
    }

    render() {
        return html`
      
      <vaadin-text-field style="width: 55%;" label="New question" ...="${field(this.binder.model.text)} "></vaadin-text-field>
      <string-array-combo-box .items="${['multiple choice']}" 
                              label="Question type"
                              value="multiple choice"
                              @value-changed="${(e : CustomEvent) => this.selectedPossibleAnswers(e)}">
      </string-array-combo-box>
      <vaadin-button @click="${this.saveQuestion}">Add</vaadin-button>
    `;
    }

// <vaadin-text-field disabled style="width: 80%;" label="surveyId" ...="${field(this.binder.model.surveyId)} "></vaadin-text-field>

    connectedCallback() {
        super.connectedCallback();

        // setting the surveyId
        this.binder.value.surveyId = this.surveyId;
    }

    nameChanged(e: CustomEvent) {
        this.name = e.detail.value;
    }

    private async saveQuestion() {
        try {
            await this.binder.submitTo(SurveyEndpoint.addQuestion);
            showNotification('Question stored.', { position: 'bottom-start' });

            await this.binder.clear();

            let event = new CustomEvent('surveyStep-added-event', {
                detail: {
                    message: 'A surveyStep was added, please refresh the view.'
                }
            });
            this.dispatchEvent(event);

            // // This should notify the encompassing component.
            // this.dispatchEvent(new CustomEvent('component-changed', {
            //     // detail: {...e.detail},
            //     bubbles: true,
            //     composed: true, // this is probably not needed but you might want it in some cases
            // }));

        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }

    async selectedPossibleAnswers(e : CustomEvent){
        console.log('Caught ' + e.detail.value);
        // this.binder.value.possibleAnswers.
    }

}

customElements.define('add-question', AddQuestion);



//
//     <h2>Add new surveyStep</h2>
// <div class="form">
//     <vaadin-text-field
// label="First Name"
// ...=${field(model.questions)}
// ></vaadin-text-field>
// <vaadin-text-field
// label="Last Name"
// ...=${field(model.lastName)}
// ></vaadin-text-field>
// <vaadin-button @click=${this.addSurvey}>Add</vaadin-button>
// </div>
