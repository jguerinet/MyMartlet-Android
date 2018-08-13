/*
 * Copyright 2014-2018 Julien Guerinet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guerinet.mymartlet.ui.ebill

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.guerinet.mymartlet.R
import com.guerinet.mymartlet.model.Statement
import com.guerinet.suitcase.date.extensions.getLongDateString
import com.guerinet.suitcase.ui.BaseListAdapter
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.sql.language.SQLite
import kotlinx.android.synthetic.main.item_statement.view.*

/**
 * Adapter used for the ebill page
 * @author Julien Guerinet
 * @since 1.0.0
 */
internal class EbillAdapter : BaseListAdapter<Statement>(ItemCallback()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StatementHolder =
            StatementHolder(viewGroup)

    /**
     * Updates the list of [Statement]s shown
     */
    fun update() {
        SQLite.select()
                .from(Statement::class)
                .async()
                .queryListResultCallback { _, tResult ->
                    submitList(tResult)
                }
                .execute()
    }

    internal class StatementHolder(parent: ViewGroup) :
            BaseHolder<Statement>(parent, R.layout.item_statement) {

        @SuppressLint("SetTextI18n")
        override fun bind(position: Int, item: Statement) {
            itemView.apply {
                date.text = context.getString(R.string.ebill_statement_date,
                        item.date.getLongDateString())
                dueDate.text = context.getString(R.string.ebill_due_date,
                        item.dueDate.getLongDateString())

                amount.text = "$${item.amount}"

                // Change the color to green or red depending on if the user owes money or not
                val colorId = if (item.amount < 0) R.color.green else R.color.red
                amount.setTextColor(ContextCompat.getColor(context, colorId))
            }
        }
    }

    class ItemCallback : DiffUtil.ItemCallback<Statement>() {

        override fun areItemsTheSame(oldItem: Statement, newItem: Statement): Boolean =
                oldItem.date == newItem.date && oldItem.dueDate == newItem.dueDate &&
                        oldItem.amount == newItem.amount

        override fun areContentsTheSame(oldItem: Statement, newItem: Statement): Boolean =
                areItemsTheSame(oldItem, newItem)
    }
}