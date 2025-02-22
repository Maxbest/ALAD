/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.sf.jclal.util.statisticalTest.statistical;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class statTableRenderer extends DefaultTableCellRenderer {

    DecimalFormat formatter;

    /**
     * Builder
     */
    public statTableRenderer() { 
        
        super();

        formatter = (DecimalFormat)DecimalFormat.getNumberInstance();

        formatter.setMinimumFractionDigits(1);
        formatter.setMaximumFractionDigits(10);

        formatter.setGroupingUsed(false);

    }

    /**
     * Formats a value on the cell
     *
     * @param value to set
     */
    public void setValue(Object value) {

        setText(formatter.format(value));
    }

    /**
     * Gets the cell renderer component used
     *
     * @param table Table to modify
     * @param value Value to represent
     * @param isSelected Tests if the cell is currently selected
     * @param hasFocus Tests if the cell has the focus
     * @param row Row selected
     * @param col Column selected
     *
     * @return Cell renderer component
     */
    public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus,int row, int col) {

        String _formattedValue;

        if(col>0){

            Double _value = (Double)value;
            if (value == null) {
                _formattedValue = "0.0";
            } else {
                _formattedValue = formatter.format(_value);
            }
        }
        else {

            _formattedValue=(String)value;

        }

        JLabel testLabel = new JLabel(_formattedValue, SwingConstants.RIGHT);

        if (isSelected) {
            testLabel.setBackground(table.getSelectionBackground());
            testLabel.setOpaque(true);
            testLabel.setForeground(table.getSelectionForeground());
        }
        if (hasFocus) {
            testLabel.setForeground(table.getSelectionBackground());
            testLabel.setBackground(table.getSelectionForeground());
            testLabel.setOpaque(true);
        }

        return testLabel;

  }

}

