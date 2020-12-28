import { LitElement, html, property, PropertyValues} from 'lit-element';
import '@vaadin/vaadin-combo-box/vaadin-combo-box.js';

import type { ComboBoxElement } from '@vaadin/vaadin-combo-box';
import type { ComboBoxItemModel } from '@vaadin/vaadin-combo-box/@types/interfaces';

class StringArrayComboBox extends LitElement {

    @property({ type: Array }) items : string[] = [];

    @property() value : string = '';
    @property() label : string = '';
    @property() placeholder : string = '';

    private _boundItemRenderer = this._itemRenderer.bind(this);

    render() {
        return html`
      <vaadin-combo-box
        label="${this.label}"
        value="${this.value}"
        placeholder="${this.placeholder}"
        ?disabled=${this.items.length <= 1}
        .items="${this.items}"
        .renderer="${this._boundItemRenderer}"
        item-value-path="category"
        item-label-path="category"
        @value-changed="${this._valueChangedHandler}"
      ></vaadin-combo-box>
    `;
    }

    protected updated(_changedProperties: PropertyValues) {
        super.updated(_changedProperties);

    }

    _valueChangedHandler(e: CustomEvent) {
        this.dispatchEvent(new CustomEvent('value-changed', {
            detail: {...e.detail},
            bubbles: true,
            composed: true, // this is probably not needed but you might want it in some cases
        }));
    }

    _itemRenderer(root: HTMLElement, _comboBox: ComboBoxElement, model: ComboBoxItemModel) {
        const item = model.item as string;
        root.innerHTML = `${item}`;
    }
}

customElements.define('string-array-combo-box', StringArrayComboBox);
