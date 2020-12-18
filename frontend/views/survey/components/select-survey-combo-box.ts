import { LitElement, html, property } from 'lit-element';
import '@vaadin/vaadin-combo-box/vaadin-combo-box.js';

import type { ComboBoxElement } from '@vaadin/vaadin-combo-box';
import type { ComboBoxItemModel } from '@vaadin/vaadin-combo-box/@types/interfaces';

class SelectSurveyComboBox extends LitElement {

    @property({ type: Array }) items : string[] = [];

    @property() value : string = '';
    @property() label : string = '';

    private _boundItemRenderer = this._itemNameRenderer.bind(this);

    render() {
        return html`
      <vaadin-combo-box
        label="${this.label}"
        value="${this.value}"
        .items="${this.items}"
        .renderer="${this._boundItemRenderer}"
        item-value-path="name"
        item-label-path="name"
        @value-changed="${this._valueChangedHandler}"
      ></vaadin-combo-box>
    `;
    }

    _valueChangedHandler(e: CustomEvent) {
        this.dispatchEvent(new CustomEvent('value-changed', {
            detail: {...e.detail},
            bubbles: true,
            composed: true, // this is probably not needed but you might want it in some cases
        }));
    }

    _itemNameRenderer(root: HTMLElement, _comboBox: ComboBoxElement, model: ComboBoxItemModel) {
        const item = model.item as string;
        root.innerHTML = `${item}`;
    }
}

customElements.define('select-survey-combo-box', SelectSurveyComboBox);
