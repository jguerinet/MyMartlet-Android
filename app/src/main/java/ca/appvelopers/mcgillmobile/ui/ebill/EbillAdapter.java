/*
 * Copyright 2014-2015 Appvelopers
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

package ca.appvelopers.mcgillmobile.ui.ebill;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ca.appvelopers.mcgillmobile.R;
import ca.appvelopers.mcgillmobile.model.Statement;
import ca.appvelopers.mcgillmobile.util.Date;

/**
 * Adapter used for the ebill page
 * @author Julien Guerinet
 * @version 2.0.1
 * @since 1.0.0
 */
public class EbillAdapter extends RecyclerView.Adapter<EbillAdapter.StatementHolder> {
    /**
     * The list of statements
     */
    private List<Statement> mStatements;

    /**
     * Default Constructor
     *
     * @param statements The list of statements
     */
    public EbillAdapter(List<Statement> statements){
        this.mStatements = statements;
    }

    @Override
    public StatementHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        return new StatementHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_statement, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(StatementHolder statementHolder, int i){
        statementHolder.bindStatement(mStatements.get(i));
    }

    @Override
    public int getItemCount(){
        return mStatements.size();
    }

    class StatementHolder extends RecyclerView.ViewHolder {
        /**
         * The statement date
         */
        @Bind(R.id.statement_date)
        TextView mDate;
        /**
         * The statement due date
         */
        @Bind(R.id.statement_due_date)
        TextView mDueDate;
        /**
         * The statement amount
         */
        @Bind(R.id.statement_amount)
        TextView mAmount;

        /**
         * Default Constructor
         *
         * @param itemView The item view
         */
        public StatementHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Binds the statement to the current view
         *
         * @param statement The statement
         */
        public void bindStatement(Statement statement){
            Context context = itemView.getContext();

            mDate.setText(context.getString(R.string.ebill_statement_date,
                    Date.getDateString(statement.getDate())));
            mDueDate.setText(context.getString(R.string.ebill_due_date,
                    Date.getDateString(statement.getDueDate())));

            double amount = statement.getAmount();
            mAmount.setText(String.format("$%s", String.valueOf(amount)));
            //Change the color to green or red depending on if the user owes money or not
            int colorId = amount < 0 ?  R.color.green : R.color.red;
            mAmount.setTextColor(ContextCompat.getColor(context, colorId));
        }
    }
}
