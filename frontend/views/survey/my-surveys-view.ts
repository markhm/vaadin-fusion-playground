import {css, customElement, html, property, LitElement, internalProperty} from 'lit-element';

import { showNotification } from '@vaadin/flow-frontend/a-notification';
import { EndpointError } from '@vaadin/flow-frontend/Connect';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid';

import '../../components/string-array-combo-box';
import './components/surveys-grid';

import * as SurveyEndpoint from "../../generated/SurveyEndpoint";
import {Router} from "@vaadin/router";
import Survey from "../../generated/fusion/playground/data/entity/Survey";

@customElement('my-surveys-view')
export class MySurveysView extends LitElement {

    @property ({type: Array})
    mySurveys: Survey[] = [];

    @internalProperty()
    newSurveyName : string = 'Blam';

    static get styles() {
        return css`
      :host {
        display: block;
        padding: 1em;
      }
    `;
    }

    render() {

        // let data = this.completedSurveys;
        // console.log('Now is the data: '+JSON.stringify(data));

        return html`                
            <h3>Your surveys</h3>
            <div>Note carefully: Published surveys no longer be edited.</div>
            <br/>
            
            <surveys-grid .surveys='${this.mySurveys}'>
            </surveys-grid>
            
            <h3>Create a new Survey</h3>
            <div>Start creating a new survey by selecting a category and survey name.</div>
            <horizontal-layout>
                <string-array-combo-box label="Category" value="example" disabled .items="${['example']}"> 
                </string-array-combo-box>
                <vaadin-text-field label="Name" @value-changed='${this.surveyNameChanged}' clear-button-visible>
                    
                </vaadin-text-field>
                <vaadin-button @click='() => ${this.createSurvey}'>Create</vaadin-button>
            </horizontal-layout>
            <br/>
            ${this.newSurveyName}
            <br/>
    `;
    }

    async connectedCallback() {
        super.connectedCallback();

        let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
        if (oktaTokenStorage === 'invalid') Router.go('/logout');

        let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;
        this.mySurveys = await SurveyEndpoint.getOwnedSurveys(oktaUserId);

        // console.log('received categories: '+JSON.stringify(this.categories));
    }

    async surveyNameChanged(e: CustomEvent) {
        this.newSurveyName = e.detail.value;

        console.log('changed event caught for text field: '+e.detail.value);

        await this.requestUpdate();
    }

    async createSurvey() {
        try {
            let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
            let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;

            let surveyId = await SurveyEndpoint.createSurvey(oktaUserId, 'example', this.newSurveyName);
            console.log("created survey: " + surveyId);
            localStorage.setItem('surveyId', surveyId);
            Router.go('edit-survey');

        } catch (error) {
            showNotification('Some error happened. ' + error.message, {position: 'bottom-start'});

            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, {position: 'bottom-start'});
            } else {
                throw error;
            }
        }
    }

}




