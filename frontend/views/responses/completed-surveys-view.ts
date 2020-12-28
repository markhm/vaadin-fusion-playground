import { css, customElement, html, property, LitElement } from 'lit-element';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid';

import './components/survey-results-grid';

import SurveyResult from "../../generated/fusion/playground/data/entity/SurveyResult";
import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import {Router} from "@vaadin/router";

// import Response from "../../generated/fusion/playground/data/entity/Response";
// import SurveyResultStatus from "../../generated/fusion/playground/data/entity/SurveyResult/SurveyResultStatus";
// import Survey from "../../generated/fusion/playground/data/entity/Survey";
// import User from "../../generated/fusion/playground/data/entity/User";
// import Question from "../../generated/fusion/playground/data/entity/Question";

@customElement('completed-surveys-view')
export class CompletedSurveysView extends LitElement {

    @property ({type: Array})
    completedSurveys: SurveyResult[] = [];

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
            <div>You completed the following surveys.</div>
            <br/>
            
            <survey-results-grid .surveyResults='${this.completedSurveys}'>

            </survey-results-grid>
            
            <br/>
    `;
    }



    async connectedCallback() {
        super.connectedCallback();

        let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
        if (oktaTokenStorage === 'invalid') Router.go('/logout');

        let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;
        this.completedSurveys = await SurveySessionEndpoint.getCompletedSurveys(oktaUserId);

        // console.log('received categories: '+JSON.stringify(this.categories));
    }
}
