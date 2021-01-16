import {css, customElement, html, internalProperty, LitElement,} from 'lit-element';
import Survey from '../../generated/fusion/playground/data/entity/Survey';

import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-button';

import './components/survey-status-combo-box';
import '../question/components/add-question';

import {Binder, field} from '@vaadin/form';
import * as SurveyEndpoint from '../../generated/SurveyEndpoint';
import SurveyModel from "../../generated/fusion/playground/data/entity/SurveyModel";

import {Router} from "@vaadin/router";
import {showNotification} from '@vaadin/flow-frontend/a-notification';
import {EndpointError} from '@vaadin/flow-frontend/Connect';
import SurveyStatus from "../../generated/fusion/playground/data/entity/Survey/SurveyStatus";
import Visibility from "../../generated/fusion/playground/data/entity/Visibility";

@customElement('edit-survey-view')
export class EditSurveyView extends LitElement {

    @internalProperty()
    private survey!: Survey;

    @internalProperty() private message = '';

    // Manages form state, binds inputs to the model
    private binder = new Binder(this, SurveyModel);

    render() {

        return html`
     <h2>Edit survey ${this.survey.name} (${this.survey.category})</h2>
     <div class="message">${this.message}</div>
     
     <vaadin-form-layout style="width: 100%;">
         
         <h3>Survey details</h3>
         <horizontal-layout>
             <vaadin-text-field label="Category" ...=${field(this.binder.model.category)} ></vaadin-text-field>
             <vaadin-text-field label="Name" ...=${field(this.binder.model.name)} ></vaadin-text-field>
             <div>Survey id: ${this.survey.id}</div>
             <vaadin-text-field label="Description" ...=${field(this.binder.model.description)}
                                style="width: 50%;"></vaadin-text-field>
         </horizontal-layout>
         
         <h3>Survey questions</h3>
         ${this.survey.surveySteps.length > 0 ? html`                 
             ${this.survey.surveySteps.map(
                     (question) => html`
                         ${question.questionNumber}: ${question.text} <a href="edit-question?questionId=${question.id}">edit</a><br/>
                     `
             )}
         `: html`No questions found yet. Add your first question.`}
         
         <add-question 
                 .surveyId="${this.survey.id}" 
                 @question-added-event="${(e : CustomEvent) => this.eventFromAddQuestion(e)}">
         </add-question>

     </vaadin-form-layout>

     <br/>
     <vaadin-horizontal-layout class="button-layout" theme="spacing">
         <vaadin-button @click="${this.saveSurvey}" theme="primary" >Save details</vaadin-button>
         <vaadin-button ?disabled="${this.survey.status === SurveyStatus.Published}"
                        @click="${this.publishSurvey}" theme="primary success" >Publish</vaadin-button>
     </vaadin-horizontal-layout>
     <br/><br/>
     <div>Note carefully: Published surveys can no longer be edited.</div>

     ${this.binder.errors.map((error) => html`${error.property}: ${error.message}`)}
     
     ${this.binder.errors ? html`             
            ${this.binder.errors.map((error) => html`${error.property}: ${error.message}`)}` :
            html``}
        `;
    }

    async connectedCallback() {
        super.connectedCallback();

         await this.loadSurvey();
    }

    async eventFromAddQuestion(e :CustomEvent) {

        console.log('component changed event caught from addSurvey-surveyStep' + e.detail.value);

        await this.reloadSurvey();
    }

    private async publishSurvey() {
        try {

            SurveyEndpoint.publishSurvey(this.survey.id);

            this.requestUpdate();

            showNotification('Published survey.', { position: 'bottom-start' });
        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }

        this.requestUpdate();
    }


    private async saveSurvey() {
        try {
            console.log('trying to save binder with content: ' + JSON.stringify(this.binder.model));
            this.binder.value.status = SurveyStatus.Draft;
            this.binder.value.visibility = Visibility.Personal;

            await this.binder.submitTo(SurveyEndpoint.update);

            showNotification('Survey stored.', { position: 'bottom-start' });
        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }


    async addSurvey() {
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

    private async reloadSurvey() {
        await this.loadSurvey();

        this.requestUpdate();
    }
    private async loadSurvey() {

        try {
            const urlParams = new URLSearchParams(window.location.search);
            let surveyId = urlParams.get('surveyId')!;
            console.log('surveyId from url: ' + surveyId);

            if (surveyId === 'unavailable' || surveyId === undefined || surveyId === null) {
                // load from url instead
                // https://www.sitepoint.com/get-url-parameters-with-javascript/

                surveyId = localStorage.getItem('surveyId') || 'unavailable';
                console.log('surveyId from session: '+surveyId);
            }

            this.survey = await SurveyEndpoint.get(surveyId) || undefined!;

            // read the current values into the binder
            console.log('found survey: ' + JSON.stringify(this.survey));

            this.binder.read(this.survey);

            if (surveyId === 'unavailable') {
                console.log('Could not find this survey, please select again.');
                Router.go('/my-surveys');
            }

        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }

    static styles = css`
   :host {
     display: block;
     padding: var(--lumo-space-m) var(--lumo-space-l);
   }
 `;
}
