import { LitElement, html, property, internalProperty, PropertyValues} from 'lit-element';
import '@vaadin/vaadin-combo-box/vaadin-combo-box.js';

import type { ComboBoxElement } from '@vaadin/vaadin-combo-box';
import type { ComboBoxItemModel } from '@vaadin/vaadin-combo-box/@types/interfaces';
import * as SurveySessionEndpoint from "../../../generated/SurveySessionEndpoint";
import QuestionResponse from "../../../generated/fusion/playground/data/entity/QuestionResponse";
import SurveyInfo from "../../../generated/fusion/playground/data/entity/SurveyInfo";

class CompletedResponses extends LitElement {

    @property({ type: String })
    surveyResultId : string = '';

    @internalProperty()
    surveyInfo : SurveyInfo = undefined!;

    @internalProperty()
    questionResponses : Array<QuestionResponse> | undefined = undefined!;

    @internalProperty()
    userHasConfirmedResponses : boolean = false;

    @property() value : string = '';
    @property() label : string = '';

    // private _boundItemRenderer = this._itemRenderer.bind(this);

    render() {
        return html`                
            <div>Your responses to survey '${this.surveyInfo.name} (${this.surveyInfo.category}):'</div>
            <br/>

            ${this.surveyInfo.gradable && this.userHasConfirmedResponses ?
                html`
                    ${this.questionResponses ? this.questionResponses.map(questionResponse =>
                    html`
                        ${questionResponse.questionNumber}: ${questionResponse.questionText} 
                        ${questionResponse.correct ? 
                            html`
                                <span style='color:green'> <b>${questionResponse.responseText}</b></span> </br>
                            ` : html`
                                <span style='color:red'> <b>${questionResponse.responseText}</b></span> </br>
                            `} 
                    `)
                    : html`Loading your results.`}`
                : html`
                        ${this.questionResponses ? this.questionResponses.map(questionResponse => 
                                html`
                            ${questionResponse.questionNumber}: ${questionResponse.questionText} <b>${questionResponse.responseText}</b> </br>
            `) : html`Loading your answers.`}`
        }
        `;
    }

    async connectedCallback() {
        super.connectedCallback();

        this._loadData();
    }

    protected updated(_changedProperties: PropertyValues) {
        super.updated(_changedProperties);

    }

    _itemRenderer(root: HTMLElement, _comboBox: ComboBoxElement, model: ComboBoxItemModel) {
        const item = model.item as string;
        root.innerHTML = `${item}`;
    }

    async _loadData() {
        this.userHasConfirmedResponses = await SurveySessionEndpoint.userHasConfirmedResponses(this.surveyResultId) || false;
        this.questionResponses = await SurveySessionEndpoint.getSurveyResponses(this.surveyResultId) || undefined;
        this.surveyInfo = await SurveySessionEndpoint.getSurveyInfo(this.surveyResultId) || undefined;
    }

}

customElements.define('completed-responses', CompletedResponses);
