import { LitElement, html, property } from 'lit-element';
import '@vaadin/vaadin-combo-box/vaadin-combo-box.js';

import type { ComboBoxElement } from '@vaadin/vaadin-combo-box';
import type { ComboBoxItemModel } from '@vaadin/vaadin-combo-box/@types/interfaces';
import SurveyStatus from "../../../generated/fusion/playground/data/entity/Survey/SurveyStatus";

// NB: This component (which contains SurveyStatus.* elements) does not work in combination with the binder

class SurveyStatusComboBox extends LitElement {

    @property({ type: Array }) items : SurveyStatus[] = [SurveyStatus.Draft, SurveyStatus.Published];

    @property() value : string = '';
    @property() label : string = '';

    private _boundItemRenderer = this._itemNameRenderer.bind(this);

    render() {
        return html`
      <vaadin-combo-box
        label="${this.label}"
        value="${this.value}"
        // ?disabled=${this.value === 'published'}
        .renderer="${this._boundItemRenderer}"
<!--        item-value-path="status"-->
<!--        item-label-path="status"-->
        @value-changed="${this._valueChangedHandler}"
      ></vaadin-combo-box>
    `;
    }

    _valueChangedHandler(e: CustomEvent) {
        console.log('The detail of the CustomEvent is: ' + JSON.stringify(e.detail));
        console.log('The detail of the CustomEvent is: ' + JSON.stringify(e.detail.value));

        this.dispatchEvent(new CustomEvent('value-changed', {
            detail: {...e.detail},
            bubbles: true,
            composed: true, // this is probably not needed but you might want it in some cases
        }));

        this.requestUpdate();
    }

    _itemNameRenderer(root: HTMLElement, _comboBox: ComboBoxElement, model: ComboBoxItemModel) {
        const value = model.item as string;
        console.log('model.item: '+model.item);
        root.innerHTML = `${value.valueOf()}`;
    }
}

customElements.define('survey-status-combo-box', SurveyStatusComboBox);
