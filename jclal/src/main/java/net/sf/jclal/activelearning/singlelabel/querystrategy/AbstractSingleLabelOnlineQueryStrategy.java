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
package net.sf.jclal.activelearning.singlelabel.querystrategy;

import java.util.ArrayList;

import net.sf.jclal.activelearning.querystrategy.AbstractQueryStrategy;
import net.sf.jclal.dataset.WekaDataset;
import net.sf.jclal.util.sort.Container;
import net.sf.jclal.util.sort.OrderUtils;

/**
 * Abstract class for single label active learning strategies.
 *
 */
public abstract class AbstractSingleLabelOnlineQueryStrategy extends AbstractSingleLabelQueryStrategy {

	private static final long serialVersionUID = 1L;
	
	private int numPassedInstances = 0; 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateLabeledData() {
		System.out.println("here in AbstractSingleLabelOnlineQueryStrategy.updateLabeledData，numPassedInstances="+numPassedInstances);
		ArrayList<Container> ordered = new ArrayList<Container>();

		// Adds the instances to labeled set
		for (int index : getSelectedInstances()) {
			getLabelledData().add(getUnlabelledData().instance(index));
			ordered.add(new Container(index, index));
		}

		// To order the array in descendant order
		//这里之所以要倒叙，是为了后面删除时按照编号删除，每删除一个，不会影响下一个编号。如果是数据流，不应该这样删除，而是应该把已经流过的numPassedInstances个数据全部删除
		OrderUtils.mergeSort(ordered, true);

		// Removes the instances from unlabeled set. The deleting operation must
		// be in descendant order
		for(int i=0; i<numPassedInstances; i++) {
			getUnlabelledData().remove(0);
		}
		//for (Container pairValue : ordered) {
			//unlabelledData=new WekaDataset(getUnlabelledData(), numPassedInstances, getUnlabelledData().getNumInstances() - numPassedInstances));
		//	getUnlabelledData().remove(Integer.parseInt(pairValue.getValue().toString()));
		//}
		
		setTestData(new WekaDataset(getUnlabelledData(), 0, numPassedInstances));
		setNumberOfSelectedInstances(getSelectedInstances().size());

		// Clears the indexes of selected instances
		getSelectedInstances().clear();
		
		

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void algorithmFinished() {
	}



	public int getNumPassedInstances() {
		return numPassedInstances;
	}
	
	public void setNumPassedInstances(int a) {
		numPassedInstances = a;
	}

}
