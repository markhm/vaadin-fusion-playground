import {css, customElement, html, internalProperty, LitElement} from 'lit-element';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';

import '../../components/string-array-combo-box';
import './components/select-survey-combo-box';

import * as SurveyEndpoint from "../../generated/SurveyEndpoint";
import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import {EndpointError} from '@vaadin/flow-frontend/Connect';
import {showNotification} from '@vaadin/flow-frontend/a-notification';
import {Router} from "@vaadin/router";

@customElement('select-survey-view')
export class SelectSurveyView extends LitElement {

    @internalProperty ()
    private categories: string[] = ['example'];

    @internalProperty ()
    selectedCategory: string = this.categories[0];

    @internalProperty ()
    private names: string[] = ['weather', 'maths', 'example'];

    @internalProperty ()
    private selectedName: string = '';

    @internalProperty ()
    private surveyDescription: string = '';

    @internalProperty ()
    private surveyResultId: string = '';

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
            <h3>Available surveys</h3>
            <div>Choose a survey.</div>

            <div class="form">
                <vaadin-horizontal-layout>

                    <string-array-combo-box label="Category" value="example" disabled
                                               .items="${this.categories}" @value-changed="${this.categorySelected}">
                    </string-array-combo-box>
                    
                    <select-survey-combo-box label="Survey name" value="example" 
                              .items="${this.names}" @value-changed="${this.nameSelected}">
                    </select-survey-combo-box>
                    
                </vaadin-horizontal-layout>
                
                ${this.selectedCategory && this.selectedName ? html`<br/><br/><div>${this.surveyDescription}</div>` : html``}
                
                <br/>
                <vaadin-button ?disabled=${this.selectedName === ''} @click='() => ${this.startSelectedSurvey}'>Start survey</vaadin-button>
            </div>
    `;
    }

    async connectedCallback() {
        super.connectedCallback();

        // moved initialization to firstUpdated();
    }

    async firstUpdated() {

        // get the list of available surveys from the endpoint
        await this.getAvailableSurveys();

        console.log('received names: ' + this.names);

        this.requestUpdate();
    }


    categorySelected(e: CustomEvent) {
        this.selectedCategory = e.detail.value;
        // console.log('selected category: '+this.selectedCategory);
        this.requestUpdate();
    }

    async nameSelected(e: CustomEvent) {
        this.selectedName = e.detail.value;
        // console.log('selected name: '+this.selectedName);

        await this.loadSurveyDescription();
        this.requestUpdate();
    }

    async startSelectedSurvey() {

        await this.initializeSurvey();

        Router.go('/question');
    }

    async initializeSurvey() {

        try {
            // retrieve the oktaUserId to initialize survey for this userClaims
            let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
            let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;

            console.log('Initializing survey ' + this.selectedName + ' for userClaims with oktaUserId: '+oktaUserId);
            this.surveyResultId = await SurveySessionEndpoint.beginSurvey(this.selectedName, oktaUserId);

            // https://medium.com/@nixonaugustine5/localstorage-and-sessionstorage-in-angular-app-65cda19283a0
            localStorage.setItem('surveyResultId', this.surveyResultId);
            console.log('wrote ' + this.surveyResultId + ' to localStorage.surveyResultId: ' + this.surveyResultId);

        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }

    async getAvailableSurveys() {
        try {
            this.names = await SurveyEndpoint.getAvailableSurveys();

        } catch (error) {
            showNotification('Some error happened. ' + error.message, {position: 'bottom-start'});

            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, {position: 'bottom-start'});
            } else {
                throw error;
            }
        }
    }

    async loadSurveyDescription() {
        try {
            this.surveyDescription = await SurveyEndpoint.getSurveyDescription(this.selectedCategory, this.selectedName);

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

