import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-select'
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid';

import { css, customElement, html, property, LitElement } from 'lit-element';
import SurveyResult from "../../generated/fusion/playground/data/entity/SurveyResult";
import * as ResponseEndpoint from "../../generated/ResponseEndpoint";

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

        let data = this.completedSurveys;
        // console.log('Now is the data: '+JSON.stringify(data));

        return html`                
            <div>You completed the following surveys.</div>
            <br/>
            
            <vaadin-grid items="[[data]]>">
                
                <vaadin-grid-column>
                    <template class="header">Survey name</template>
                    <template>[[data.survey.name]]</template>
                </vaadin-grid-column>

                <vaadin-grid-column>
                    <template class="header">Completed at</template>
                    <template>[[data.endTime]]</template>
                </vaadin-grid-column>

                <vaadin-grid-column>
                    <template class="header">Results approved</template>
                    <template>[[data.status]]</template>
                </vaadin-grid-column>
            </vaadin-grid>
            <br/>
            ${data.map(dataElement => html`
                ${dataElement.survey.name} : ${dataElement.endTime}: ${dataElement.status} <br/>
                        `)}
    `;
    }

    async connectedCallback() {
        super.connectedCallback();

        let oktaTokenStorage = localStorage.getItem('okta-token-storage') || 'invalid';
        let oktaUserId = JSON.parse(oktaTokenStorage).accessToken.claims.uid;

        this.completedSurveys = await ResponseEndpoint.getCompletedSurveys(oktaUserId);
        // console.log('received availableSurveys: '+JSON.stringify(this.availableSurveys));
    }
}




