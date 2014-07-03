/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 08/apr/2014
 * Copyright 2013-2014 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.merge;

import java.util.function.Consumer;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.pdfsam.context.DefaultI18nContext;
import org.pdfsam.support.KeyStringValueItem;
import org.pdfsam.support.params.TaskParametersBuildStep;
import org.pdfsam.ui.support.Style;
import org.sejda.model.outline.OutlinePolicy;

/**
 * Panel for the Merge options
 * 
 * @author Andrea Vacondio
 *
 */
class MergeOptionsPane extends VBox implements TaskParametersBuildStep<MergeParametersBuilder> {

    private CheckBox containsForms;
    private CheckBox blankIfOdd;
    private ComboBox<KeyStringValueItem<OutlinePolicy>> outline = new ComboBox<>();

    MergeOptionsPane() {
        super(5);
        this.containsForms = new CheckBox(DefaultI18nContext.getInstance().i18n("Merge form fields"));
        this.containsForms.setTooltip(new Tooltip(DefaultI18nContext.getInstance().i18n(
                "Some of the selected PDF documents contain forms, merge them")));
        this.blankIfOdd = new CheckBox(DefaultI18nContext.getInstance().i18n("Add a blank page if page number is odd"));
        this.blankIfOdd.setTooltip(new Tooltip(DefaultI18nContext.getInstance().i18n(
                "Adds a blank page after each merged document if the document has an odd number of pages")));

        outline.getItems().add(
                new KeyStringValueItem<>(OutlinePolicy.RETAIN, DefaultI18nContext.getInstance()
                        .i18n("Retain bookmarks")));
        outline.getItems().add(
                new KeyStringValueItem<>(OutlinePolicy.DISCARD, DefaultI18nContext.getInstance().i18n(
                        "Discard bookmarks")));
        outline.getItems().add(
                new KeyStringValueItem<>(OutlinePolicy.ONE_ENTRY_EACH_DOC, DefaultI18nContext.getInstance().i18n(
                        "Create one entry for each merged document")));
        outline.getSelectionModel().selectFirst();
        HBox bookmarksPolicy = new HBox(new Label(DefaultI18nContext.getInstance().i18n("Bookmarks handling:")),
                outline);
        bookmarksPolicy.getStyleClass().addAll(Style.VITEM.css());
        bookmarksPolicy.getStyleClass().addAll(Style.HCONTAINER.css());

        getStyleClass().addAll(Style.CONTAINER.css());
        getChildren().addAll(this.containsForms, this.blankIfOdd, bookmarksPolicy);
    }

    public void apply(MergeParametersBuilder builder, Consumer<String> onError) {
        builder.outlinePolicy(outline.getSelectionModel().getSelectedItem().getKey());
        builder.blankPageIfOdd(blankIfOdd.isSelected());
        builder.copyFormFields(containsForms.isSelected());
    }
}