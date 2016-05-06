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
package net.sf.jclal.activelearning.batchmode;

import java.util.ArrayList;
import java.util.List;
import net.sf.jclal.core.IQueryStrategy;
import net.sf.jclal.util.sort.Container;
import net.sf.jclal.util.sort.OrderUtils;

/**
 * Implementation of Q-best batch construction.
 *
 * Querying the Q-best queries according to some query strategy often does not
 * work well, since it fails to consider the overlap in information content
 * among the 鈥渂est鈥� instances.
 *
 * If only one (1) instance is selected, then the queries are selected in
 * serial, i.e., one at a time, corresponding to the most active learning
 * research.
 *
 * See for more information: Settles, B. (2010). Active Learning Literature
 * Survey (p. 67). Section 6.1.
 *
 * @author Oscar Gabriel Reyes Pupo
 * @author Eduardo Perez Perdomo
 */
public class QBestBatchMode extends AbstractBatchMode {

	private static final long serialVersionUID = 1L;

	/**
     * Order the instances and return according to the type of active learning strategy.
     *
     * @param queryStrategy To analyze if the strategy is maximal or minimal
     * @param intanceUtilities The utility value of the unlabeled instances
     * @return The indexes of the selected instances
     * 这里返回的是一个整形的list，每一项是container中的每一项的getValue.tostring，首先判断是找最小的还是最大的
     * 然后排序，按照排序挑出前面的一些，按照batchsize多少挑出来
     * 问题：如果是stream场景，那么batch大小就是时间窗口的大小了吗？这样每一个batch都会被全部选中了啊？是不是hold out方法不适合stream场景？
     */
    @Override
    public List<Integer> instancesSelection(IQueryStrategy queryStrategy, List<Container> intanceUtilities) {

        OrderUtils.mergeSort(intanceUtilities, queryStrategy.isMaximal());

        List<Integer> selected = new ArrayList<Integer>();

        for (Container toOrder : intanceUtilities) {

            if (selected.size() < getBatchSize()) {

                selected.add(Integer.parseInt(toOrder.getValue().toString()));

            } else {
                break;
            }
        }

        return selected;
    }
}
