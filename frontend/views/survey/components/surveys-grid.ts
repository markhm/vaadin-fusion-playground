import { LitElement, property, html, query} from 'lit-element';

import { render } from 'lit-html';
import '@vaadin/vaadin-grid/vaadin-grid.js';
import '@vaadin/vaadin-checkbox/vaadin-checkbox.js';

import type { CheckboxElement } from '@vaadin/vaadin-checkbox';
import type { GridElement, GridItem, GridItemModel } from '@vaadin/vaadin-grid';
import type { GridColumnElement } from '@vaadin/vaadin-grid/vaadin-grid-column.js';

import Survey from "../../../generated/fusion/playground/data/entity/Survey";

const itemCache = new WeakMap<HTMLElement>();

class SurveysGrid extends LitElement {
    @property({ type: Array })
    surveys = [];

    @query('vaadin-grid')
    private grid!: GridElement;

    private _boundToggleDetailsRenderer = this._toggleDetailsRenderer.bind(this);
    private _boundRowDetailsRenderer = this._rowDetailsRenderer.bind(this);

    //
    render() {
        return html`
      <vaadin-grid .items="${this.surveys}" .rowDetailsRenderer="${this._boundRowDetailsRenderer}">
          <vaadin-grid-column header='Survey name (category)' .renderer="${this._surveyNameCategoryRenderer}"></vaadin-grid-column>
          <vaadin-grid-column header='Status' .renderer="${this._statusRenderer}"></vaadin-grid-column>
          <vaadin-grid-column header='Last column' .renderer="${this._boundToggleDetailsRenderer}"></vaadin-grid-column>
      </vaadin-grid>
    `;
    }

    connectedCallback() {
        super.connectedCallback();
    }

    _onCheckboxChange(e: CustomEvent) {
        const checkbox = e.target as HTMLElement;
        this._toggleDetails(e.detail.value, itemCache.get(checkbox.parentNode as HTMLElement));
    }

    _toggleDetails(value: boolean, item: GridItem) {
        if (value) {
            this.grid.openItemDetails(item);
        } else {
            this.grid.closeItemDetails(item);
        }
    }

    _toggleDetailsRenderer(root: HTMLElement, _column: GridColumnElement, model: GridItemModel) {
        // only render the checkbox once, to avoid re-creating during subsequent calls
        if (!root.firstElementChild) {
            render(
                html`
          <vaadin-checkbox @checked-changed="${this._onCheckboxChange}">
            Show description
          </vaadin-checkbox>
        `,
                root,
                { eventContext: this } // bind event listener properly
            );
        }
        const { item } = model;
        // store the item to avoid grid virtual scrolling reusing DOM nodes to mess it up
        itemCache.set(root, item);
        const detailsOpened = this.grid.detailsOpenedItems || [];
        (root.firstElementChild as CheckboxElement).checked = detailsOpened.indexOf(item) > -1;
    }

    editSurvey(surveyId: string) {
        console.log('About to edit survey with Id: '+surveyId);
    }

    _surveyNameCategoryRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const survey = model.item as Survey;

        if (survey.status === 'draft') {
            render(html`<a href="edit-survey?surveyId=${survey.id}">${survey.name}</a> (${survey.category}) 
<!--            <vaadin-button class="size-xs" @click="this.buttonClick">-->
<!--                <iron-icon icon="lumo:edit" slot="prefix"></iron-icon>Edit-->
<!--            </vaadin-button>-->
            `, root);
        }
        else {
            render(html`${survey.name} (${survey.category})`, root);
        }

    }

    buttonClick(e: CustomEvent){
        const button = e.target as HTMLElement;
        console.log('customEvent for buttonClick: '+e.detail.value);
        this._toggleDetails(e.detail.value, itemCache.get(button.parentNode as HTMLElement));
    }

    _statusRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const survey = model.item as Survey;
        render(html`${survey.status}`, root);
    }

    _rowDetailsRenderer(root: HTMLElement, _grid: GridElement, model: GridItemModel) {
        const survey = model.item as Survey;
        render(html`
            ${survey.description}
        `, root);
    }

    // editSurvey = (surveyId: string) => {
    //     console.log('About to edit survey with Id: '+surveyId);
    // }
}

customElements.define('surveys-grid', SurveysGrid);
