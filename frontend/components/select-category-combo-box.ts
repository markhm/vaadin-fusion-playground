import { LitElement, html, property } from 'lit-element';
import '@vaadin/vaadin-combo-box/vaadin-combo-box.js';

import type { ComboBoxElement } from '@vaadin/vaadin-combo-box';
import type { ComboBoxItemModel } from '@vaadin/vaadin-combo-box/@types/interfaces';

type Survey = { category: string; name: string };

class SelectCategoryComboBox extends LitElement {

    @property({ type: Array }) surveys : Survey[] = [
            {   category: 'example', name: 'example'},
            {   category: 'example', name: 'maths'},
            {   category: 'example', name: 'weather'}
        ];

    @property() value : string = 'maths';

    @property() disabled : boolean = false;

    @property() label : string = 'Label';

    private _boundItemRenderer = this._surveyCategoryRenderer.bind(this);

    // // disabled="${this.disabled}" <- This did not work

    render() {
        return html`
      <vaadin-combo-box
        label="${this.label}"
        value="${this.value}"
        .items="${this.surveys}"
        .renderer="${this._boundItemRenderer}"
        item-value-path="category"
        item-label-path="category"
      ></vaadin-combo-box>
    `;
    }

    _surveyCategoryRenderer(root: HTMLElement, _comboBox: ComboBoxElement, model: ComboBoxItemModel) {
        const survey = model.item as Survey;
        root.innerHTML = `${survey.category}`;
    }
}

customElements.define('select-survey-combo-box', SelectCategoryComboBox);
