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
import SurveyInfo from "../../generated/fusion/playground/data/entity/SurveyInfo";

@customElement('select-survey-view')
export class SelectSurveyView extends LitElement {

    @internalProperty ()
    private categories: string[] = ['example'];

    // @internalProperty ()
    // private names: string[] = ['weather', 'maths', 'example'];

    @internalProperty()
    private surveyInfos: SurveyInfo[] = [];

    @internalProperty ()
    selectedCategory: string = this.categories[0];

    @internalProperty ()
    private selectedSurvey: SurveyInfo = undefined!;

    @internalProperty ()
    private surveyDescription: string = '';

    @internalProperty ()
    private oktaUserId: string = '';

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
                              .items="${this.surveyInfos}" @value-changed="${this.surveySelected}">
                    </select-survey-combo-box>
                    
                </vaadin-horizontal-layout>

                <br/>
                ${this.selectedSurvey ? html`<div>${this.surveyDescription}</div>` : html``}
                
                <br/>
                <div>
                    <vaadin-button ?disabled=${this.selectedSurvey === undefined} @click='() => ${this.startSelectedSurvey}'>Start survey</vaadin-button>
                </div>
            </div>
    `;
    }

    async connectedCallback() {
        super.connectedCallback();

        // retrieve the oktaUserId to initialize survey for this userClaims
        let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
        this.oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;

        // moved initialization to firstUpdated();
    }

    async firstUpdated() {

        // get the list of available surveyInfos from the endpoint
        await this.getAvailableSurveys();

        this.requestUpdate();
    }

    categorySelected(e: CustomEvent) {
        this.selectedCategory = e.detail.value;
        // console.log('selected category: '+this.selectedCategory);

        this.requestUpdate();
    }

    surveySelected(e: CustomEvent) {
        let nameSelected = e.detail.value;

        console.log('surveyInfo returned: ' + JSON.stringify(e.detail.value));

        for (let i = 0; i < this.surveyInfos.length; i++) {
            if (nameSelected === this.surveyInfos[i].name) {
                this.selectedSurvey = this.surveyInfos[i];
                break;
            }
        }

        this.requestUpdate();
    }

    async startSelectedSurvey() {

        await this.initializeSurvey();

        Router.go('/question');
    }

    async getAvailableSurveys() {
        try {
            this.surveyInfos = await SurveyEndpoint.getAvailableSurveys(this.oktaUserId);

        } catch (error) {
            showNotification('Some error happened. ' + error.message, {position: 'bottom-start'});

            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, {position: 'bottom-start'});
            } else {
                throw error;
            }
        }
    }

    async initializeSurvey() {

        try {
            console.log('Initializing survey ' + this.selectedSurvey.name + ' for userClaims with oktaUserId: ' + this.oktaUserId);
            this.surveyResultId = await SurveySessionEndpoint.beginSurvey(this.selectedSurvey.surveyId, this.oktaUserId);

            // https://medium.com/@nixonaugustine5/localstorage-and-sessionstorage-in-angular-app-65cda19283a0
            localStorage.setItem('surveyResultId', this.surveyResultId);
            console.log('wrote ' + this.surveyResultId + ' to localStorage.surveyResultId: ' + this.surveyResultId);

            // storing the info for the selected survey locally, to pass it to the question-view
            localStorage.setItem('selectedSurveyInfo', JSON.stringify(this.selectedSurvey));

        } catch (error) {
            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
    }
}

