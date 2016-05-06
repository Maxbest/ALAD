package net.sf.jclal.sampling.byorder;

import net.sf.jclal.core.IDataset;
import net.sf.jclal.dataset.MulanDataset;
import net.sf.jclal.dataset.WekaDataset;
import net.sf.jclal.sampling.AbstractSampling;
import org.apache.commons.configuration.Configuration;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Produces a byorder subsample of a dataset. The number of instances in the generated
 * dataset may be specified.
 *
 * @author Guan Hongtao
 */
public class Resample extends AbstractSampling {

	/**
	 * for serialization
	 */
	static final long serialVersionUID = 1L;

	/**
	 * Whether to perform sampling with replacement or without
	 */
	protected boolean noReplacement = true;

	/**
	 * Whether to invert the selection (only if instances are drawn WITHOUT
	 * replacement)
	 *
	 */
	protected boolean invertSelection = false;

	/**
	 * Whether to perform sampling with replacement or without
	 * 
	 * @return Whether the replacement is used or not.
	 */
	public boolean isNoReplacement() {
		return noReplacement;
	}

	/**
	 * Whether to perform sampling with replacement or without
	 * 
	 * @param noReplacement
	 *            the replacement is used or not.
	 * 
	 */
	public void setNoReplacement(boolean noReplacement) {
		this.noReplacement = noReplacement;
	}

	/**
	 * Whether to invert the selection (only if instances are drawn WITHOUT
	 * replacement)
	 * 
	 * @return whether the invert selection is used or not
	 *
	 */
	public boolean isInvertSelection() {
		return invertSelection;
	}

	/**
	 * Whether to invert the selection (only if instances are drawn WITHOUT
	 * replacement)
	 * 
	 * @param invertSelection
	 *            Set the invert selection.
	 *
	 */
	public void setInvertSelection(boolean invertSelection) {
		this.invertSelection = invertSelection;
	}

	/**
	 * Creates the subsample with replacement
	 *
	 * @param dataSet
	 *            The dataset to extract a percent of instances
	 * @param sampleSize
	 *            the size to generate
	 */
	public void createSubsampleWithoutReplacement(IDataset dataSet, int sampleSize) {
		
		Instances labeledInstances = new Instances(dataSet.getDataset(), sampleSize);
		
		// Copy the entire dataset into unlabeled set
		Instances unlabeledInstances = new Instances(dataSet.getDataset());
		
		// Fill the labeled set
		for (int i = 0; i < sampleSize; i++) {
			labeledInstances.add((Instance) dataSet.instance(i).copy());
			// remove the instances that have been selected previously
			//这里的remove是按照index来删除的，要把已经放在标签数据集中的index拿来，把这些数据去掉，就形成了无标签数据集
			unlabeledInstances.remove(i);
		}

		if (dataSet instanceof WekaDataset) {
			setLabeledData(new WekaDataset(labeledInstances));
			setUnlabeledData(new WekaDataset(unlabeledInstances));
		}

		if (dataSet instanceof MulanDataset) {
			setLabeledData(new MulanDataset(labeledInstances, ((MulanDataset) dataSet).getLabelsMetaData()));
			setUnlabeledData(new MulanDataset(unlabeledInstances, ((MulanDataset) dataSet).getLabelsMetaData()));
		}

		// clean up
		unlabeledInstances.clear();
		labeledInstances.clear();

		unlabeledInstances = null;
		labeledInstances = null;

	}

	/**
	 * Creates the subsample without replacement
	 *
	 * @param dataSet
	 *            The dataset to extract a percent of instances
	 * @param sampleSize
	 *            the size to generate
	 */


	/**
	 * Sampling instances
	 * 
	 * @param dataSet
	 *            The dataset to extract the instances.
	 */
	@Override
	public void sampling(IDataset dataSet) {

		int sampleSize = (int) (dataSet.getNumInstances() * getPercentageInstancesToLabelled() / 100);

		createSubsampleWithoutReplacement(dataSet, sampleSize);
		
	}

	/**
	 *
	 * @param configuration
	 *            The configuration object for Resample.
	 * 
	 *            The XML labels supported are:
	 * 
	 *            <ul>
	 *            <li><b>no-replacement= boolean</b></li>
	 *            <li><b>invert-selection= boolean</b></li>
	 *            </ul>
	 */
	@Override
	public void configure(Configuration configuration) {

		super.configure(configuration);

		boolean noReplacementT = configuration.getBoolean("no-replacement", noReplacement);

		setNoReplacement(noReplacementT);

		boolean invert = configuration.getBoolean("invert-selection", invertSelection);

		setInvertSelection(invert);
	}
}