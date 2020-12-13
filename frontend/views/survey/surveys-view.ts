import {Router} from "@vaadin/router";
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';

import { css, customElement, html, property, LitElement } from 'lit-element';
import * as SurveyEndpoint from "../../generated/SurveyEndpoint";

@customElement('surveys-view')
export class SurveysView extends LitElement {

    @property ({type: Array})
    availableSurveys: String[] = ['example', 'weather'];

    @property ({type: String})
    selectedSurvey: String = '';

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

        this.availableSurveys = await SurveyEndpoint.getAvailableSurveys();
        console.log('received availableSurveys: '+JSON.stringify(this.availableSurveys));
    }

    surveySelected(e: CustomEvent) {
        // It is absurd that the parent event does not show its available child elements.
        // console.log('event: CustomEvent: ' + JSON.stringify(e));
        // console.log('event.detail = ' + JSON.stringify(e.detail));
        // console.log('event.detail.value = ' + e.detail.value);

        this.selectedSurvey = e.detail.value;
    }

    submit() {
        console.log('departments:' + JSON.stringify(this.availableSurveys));

        // showNotification('Selected ' + this.selectedSurvey);
        Router.go('/questions?surveyName='+this.selectedSurvey);
    }
}

