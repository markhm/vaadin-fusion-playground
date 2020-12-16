import {Router} from "@vaadin/router";
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';

import { css, customElement, html, property, LitElement } from 'lit-element';
import * as SurveyEndpoint from "../../generated/SurveyEndpoint";
import * as ResponseEndpoint from "../../generated/ResponseEndpoint";

@customElement('select-survey-view')
export class SelectSurveyView extends LitElement {

    @property ({type: Array})
    availableSurveys: String[] = ['example', 'weather', 'maths_1'];

    @property ({type: String})
    selectedSurvey: string = '';

    static get styles() {
        return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
    }

    render() {

        let data = this.availableSurveys;
        // console.log('Now is the data: '+JSON.stringify(data));

        return html`
            <h3>Available surveys</h3>
            <div>Choose a survey from the following list.</div>
            <vaadin-select id='select' label='Surveys' @value-changed='${this.surveySelected}' laceholder='none selected'>
                <template>
                    <vaadin-list-box>
                        ${data.map(dataElement => html`
                            <vaadin-item>${dataElement}</vaadin-item>
                        `)}
                    </vaadin-list-box>
                </template>
            </vaadin-select>
            
            ${this.selectedSurvey ? html`<br/><br/>You selected survey '${this.selectedSurvey}'.` : html``}
            
            <br/><br/>
            <vaadin-button @click="${this.submit}">Start survey</vaadin-button>
    `;
    }

    async connectedCallback() {
        super.connectedCallback();

        this.availableSurveys = await SurveyEndpoint.getSurveyNames();
        console.log('received availableSurveys: '+JSON.stringify(this.availableSurveys));
    }

    surveySelected(e: CustomEvent) {
        // NB: It is absurd that the parent event does not show its available child elements.
        this.selectedSurvey = e.detail.value;
    }

    async submit() {
        console.log('departments:' + JSON.stringify(this.availableSurveys));

        // showNotification('Selected ' + this.selectedSurvey);
        // localStorage.setItem('requestedSurvey', this.selectedSurvey);

        let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
        let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;
        console.log('User is indentified as: '+oktaUserId);

        let surveyResultId = await ResponseEndpoint.beginSurvey(this.selectedSurvey, oktaUserId);

        // https://medium.com/@nixonaugustine5/localstorage-and-sessionstorage-in-angular-app-65cda19283a0
        localStorage.setItem('surveyResultId', surveyResultId);
        localStorage.setItem('surveyStatus', 'new');
        console.log('wrote '+surveyResultId+' to localStorage.surveyResultId: ' + surveyResultId);

        Router.go('/question');
    }
}

