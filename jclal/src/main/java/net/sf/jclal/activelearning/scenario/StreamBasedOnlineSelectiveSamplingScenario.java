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
package net.sf.jclal.activelearning.scenario;

import java.util.ArrayList;
import java.util.List;
import net.sf.jclal.activelearning.batchmode.AbstractBatchMode;
import net.sf.jclal.activelearning.querystrategy.AbstractQueryStrategy;
import net.sf.jclal.activelearning.singlelabel.querystrategy.AbstractSingleLabelOnlineQueryStrategy;
import net.sf.jclal.util.sort.Container;
import org.apache.commons.configuration.Configuration;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Implementation of Stream-Based Online Sampling scenario. This scenario is sometimes
 * called sequential active.
 *
 * @author Oscar Gabriel Reyes Pupo
 * @author Eduardo Perez Perdomo
 *
 */
public class StreamBasedOnlineSelectiveSamplingScenario extends AbstractScenario {

	private static final long serialVersionUID = 1L;
	/*
	 * The threshold must be configured in dependence of the query strategy. For
	 * example:
	 * 
	 * UncertaintySampling=0.5 DensityDiversity=0.7
	 * EntropySamplingQueryStrategy=0.7 LeastConfidence=0.7 ErrorReduction=0.39
	 * MarginSampling=0.7 RandomSampling=1 RelevanceSampling=0.7
	 * 
	 */
	private double threshold = 0.5;
	
	private double values[];

	/**
	 * Get the threshold
	 *
	 * @return The threshold used when the scenario is stream.
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * 
	 * Set the threshold
	 * 
	 * @param threshold
	 *            The threshold used when the scenario is stream.
	 *
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void instancesSelection() {
		
		// found values of all unlabeled instances
		//values = getQueryStrategy().testUnlabeledData();

		Instances unlabelled = getQueryStrategy().getUnlabelledData().getDataset();

		List<Container> selection = new ArrayList<Container>();
		
		for (int i = 0; i < unlabelled.numInstances(); i++) {
			
			//手工添加的用来记录已经有多少个用例passed掉的变量
			((AbstractSingleLabelOnlineQueryStrategy) getQueryStrategy()).setNumPassedInstances(i+1);

			Instance in = unlabelled.instance(i);

			double test = getQueryStrategy().utilityInstance(in);

			if (getQueryStrategy().isMaximal() && test >= threshold) {
				selection.add(new Container(test, i));
			} else if (!getQueryStrategy().isMaximal() && test <= threshold) {
				selection.add(new Container(test, i));
			}

			if (selection.size() == ((AbstractBatchMode) getBatchMode()).getBatchSize()) {
				break;
			}
		}

		((AbstractQueryStrategy) getQueryStrategy()).getSelectedInstances()
				.addAll(getBatchMode().instancesSelection(getQueryStrategy(), selection));
	}

	/**
	 *
	 * @param configuration
	 *            The configuration of Stream Scenario. The XML labels supported
	 *            are:
	 *            <ul>
	 *            <li><b>threshold= double</b></li>
	 *            </ul>
	 */
	@Override
	public void configure(Configuration configuration) {

		super.configure(configuration);

		// Set threshold
		double thresholdT = configuration.getDouble("threshold", threshold);

		setThreshold(thresholdT);
	}
}
