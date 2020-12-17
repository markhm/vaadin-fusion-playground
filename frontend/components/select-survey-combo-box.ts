import { LitElement, html, property } from 'lit-element';
import '@vaadin/vaadin-combo-box/vaadin-combo-box.js';

import type { ComboBoxElement } from '@vaadin/vaadin-combo-box';
import type { ComboBoxItemModel } from '@vaadin/vaadin-combo-box/@types/interfaces';

type Survey = { category: string; name: string };

class SelectSurveyComboBox extends LitElement {

    @property({ type: Array }) surveys : Survey[] = [
            {   category: 'example', name: 'example'},
            {   category: 'example', name: 'maths'},
            {   category: 'example', name: 'weather'}
        ];

    @property() value : string = 'maths';

    @property() disabled : boolean = false;

    @property() label : string = 'Label';

    private _boundItemRenderer = this._surveyNameRenderer.bind(this);

    // // disabled="${this.disabled}" <- This did not work

    render() {
        return html`
      <vaadin-combo-box
        label="${this.label}"
        value="${this.value}"
        .items="${this.surveys}"
        .renderer="${this._boundItemRenderer}"
        item-value-path="name"
        item-label-path="name"
      ></vaadin-combo-box>
    `;
    }

    _surveyNameRenderer(root: HTMLElement, _comboBox: ComboBoxElement, model: ComboBoxItemModel) {
        const survey = model.item as Survey;
        root.innerHTML = `${survey.name}`;
    }
}

customElements.define('select-survey-combo-box', SelectSurveyComboBox);
