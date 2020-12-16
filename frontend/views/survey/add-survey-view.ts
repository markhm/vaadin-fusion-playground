import { LitElement, html, css, customElement, internalProperty, } from 'lit-element';
import Survey from '../../generated/fusion/playground/data/entity/Survey';
import Question from '../../generated/fusion/playground/data/entity/Question';

import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-button';
import { Binder, field } from '@vaadin/form';
// import { addQuestion, getSurvey } from '../../generated/SurveyEndpoint';
import * as SurveyEndpoint from '../../generated/SurveyEndpoint';
import SurveyModel from "../../generated/fusion/playground/data/entity/SurveyModel";


@customElement('add-survey-view')
export class AddSurveyView extends LitElement {

    @internalProperty() private survey: Survey = {
        id: '-1',
        category: 'test',
        name: 'unset',
        questions: [],
        gradable: false
    };

    @internalProperty() private questions: Question[] = this.survey.questions;

    @internalProperty() private message = '';

    // Manages form state, binds inputs to the model
    private binder = new Binder(this, SurveyModel);

    render() {
        const { model } = this.binder;

        // let currentQuestion = this.survey.questions[0];

        return html`
     <h1>Survey</h1>

     <div class="message">${this.message}</div>

     <vaadin-text-field label="Survey" ...=${this.survey}></vaadin-text-field>
     
     <ul>
       ${this.questions.map(
            (question) => html`<li>${question.orderNumber} ${question.text}</li>`
        )}
     </ul>

     <h2>Add new question</h2>
     <div class="form">
       <vaadin-text-field label="Category" ...=${field(model.category)}></vaadin-text-field>
       <vaadin-text-field label="Name" ...=${field(model.name)}></vaadin-text-field>
       <vaadin-button @click=${this.add}>Add</vaadin-button>
     </div>
   `;
    }

    async connectedCallback() {
        super.connectedCallback();
        try {
            const emptySurvey = {
                id: '-1',
                category: 'test',
                name: 'unset',
                questions: [],
                gradable: false
            };

            this.survey = await SurveyEndpoint.get(this.survey.id) || emptySurvey;
            this.questions = this.survey.questions;
        } catch (e) {
            this.message = `Failed to retrieve questions for Survey: ${e.message}.`;
        }
    }

    async add() {
        try {
            const saved = await this.binder.submitTo(SurveyEndpoint.update);
            if (saved) {
                // this.questions = [...this.questions, saved];
                this.binder.clear();
            }
        } catch (e) {
            this.message = `Failed to save: ${e.message}.`;
        }
    }

    static styles = css`
   :host {
     display: block;
     padding: var(--lumo-space-m) var(--lumo-space-l);
   }
 `;
}
