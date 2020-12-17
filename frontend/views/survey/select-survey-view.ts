import {css, customElement, html, internalProperty, LitElement} from 'lit-element';

import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';

import * as SurveyEndpoint from "../../generated/SurveyEndpoint";
import * as SurveySessionEndpoint from "../../generated/SurveySessionEndpoint";
import {EndpointError} from '@vaadin/flow-frontend/Connect';
import {showNotification} from '@vaadin/flow-frontend/a-notification';
import {Router} from "@vaadin/router";

@customElement('select-survey-view')
export class SelectSurveyView extends LitElement {

    @internalProperty ()
    private categories: string[] = ['example'];

    @internalProperty () private names: string[] = ['weather', 'maths', 'example'];

    @internalProperty ()
    selectedCategory: string = 'example';

    @internalProperty ()
    private selectedName: string = '';

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

        let categoriesItems = this.categories.map(item => html`<vaadin-item>${item}</vaadin-item>`);
        let namesItems = this.names.map(item => html`<vaadin-item>${item}</vaadin-item>`);

        console.log('namesItems: '+ JSON.stringify(namesItems));

        return html`                
            <h3>Available surveys</h3>
            <div>Choose a survey.</div>

            <div class="form">
                <vaadin-horizontal-layout>
                    <vaadin-select label='Category' 
                                   @value-changed='() => ${this.categorySelected}' 
                                   placeholder='none selected' value='example'>
                        <template>
                            <vaadin-list-box>
                               ${categoriesItems}
                            </vaadin-list-box>
                        </template>
                    </vaadin-select>
                    
                    <vaadin-select label='Survey name' @value-changed='() => ${this.nameSelected}' placeholder='none selected'>
                        <template>
                            <vaadin-list-box>
                                ${namesItems}
                            </vaadin-list-box>
                        </template>
                    </vaadin-select>
                </vaadin-horizontal-layout>
                
                ${this.selectedCategory ? html`<br/><br/>You selected category '${this.selectedCategory}'.` : html``}
                ${this.selectedName ? html`<br/>You selected survey '${this.selectedName}'.` : html``}
                
                <br/><br/>
                <vaadin-button ?disabled=${this.selectedName === ''} @click='() => ${this.startSelectedSurvey}'>Start survey</vaadin-button>
            </div>
    `;
    }

    // ?disabled=${categoriesItems.length <= 1}
    // ?disabled=${this.names.length === 0}
// <template>
// <vaadin-list-box>
//     ${namesItems}
// </vaadin-list-box>
// </template>

    async connectedCallback() {
        super.connectedCallback();

        // await this.getAvailableSurveys();
        // console.log('received names: '+JSON.stringify(this.names));
        this.requestUpdate();
    }

    async firstUpdated() {

        console.log('firstUpdated() called');
        this.names = await this.getAvailableSurveys() || ['one', 'two', 'and another one'];

        console.log('received names: ' + this.names);

        this.requestUpdate();
    }


    categorySelected(e: CustomEvent) {
        this.selectedCategory = e.detail.value;
        console.log('selected category: '+this.selectedCategory);
        this.requestUpdate();
    }

    nameSelected(e: CustomEvent) {
        this.selectedName = e.detail.value;
        console.log('selected name: '+this.selectedName);
        this.requestUpdate();
    }

    async startSelectedSurvey() {

        await this.initializeSurvey();

        Router.go('/question');
    }

    async initializeSurvey() {

        try {
            // retrieve the oktaUserId to initialize survey for this user
            let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
            let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;

            console.log('Initializing survey ' + this.selectedName + ' for user with oktaUserId: '+oktaUserId);
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
            let result = await SurveyEndpoint.getAvailableSurveys();
            return result;

        } catch (error) {
            showNotification('Some error happened. ' + error.message, { position: 'bottom-start' });

            if (error instanceof EndpointError) {
                showNotification('Server error. ' + error.message, { position: 'bottom-start' });
            } else {
                throw error;
            }
        }
        return ['one', 'two', 'and another one'];
    }

}

