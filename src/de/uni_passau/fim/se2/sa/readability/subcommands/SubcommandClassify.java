package de.uni_passau.fim.se2.sa.readability.subcommands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

@Command(name = "classify", description = "Classify readability of .jsnp Java snippets")
public class SubcommandClassify implements Callable<Integer> {

    @Spec
    CommandSpec spec;

    private File data;

    @Option(names = {"-d", "--data"}, description = "The data .csv file to train the model on.",
            required = true)
    public void setDataFile(File dataFile) {
        if (!dataFile.exists() || !dataFile.isFile()) {
            throw new ParameterException(spec.commandLine(),
                    "The data file does not exist or is not a file.");
        }
        data = dataFile;
    }

    public Integer call() {
        try {

            Instances dataset = loadDataset();

            Evaluation eval = trainAndEvaluate(dataset);

            printResults(eval);

            return 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 1;
        }
    }


    /**
     * Loads the instances dataset by parsing the CSV file specified via the cli.
     *
     * @return the instances dataset ready to be classified.
     * @throws Exception if the CSV file specified via the cli could not be loaded.
     */
    private Instances loadDataset() throws Exception {
        var source = new DataSource(data.getAbsolutePath());
        var dataset = source.getDataSet();
        dataset.setClassIndex(dataset.numAttributes() - 1);
        return dataset;
    }

    /**
     * Trains and evaluates the "logistic" classifier on the given dataset. For the evaluation, we
     * apply a 10-fold cross validation using a start seed with a value of 1.
     *
     * @param dataset The dataset to train and evaluate the logistic classifier on.
     * @return the evaluation object hosting the evaluation results.
     * @throws Exception if the classifier could not be generated successfully.
     */
    private static Evaluation trainAndEvaluate(Instances dataset) throws Exception {
        var model = new Logistic();
        var eval = new Evaluation(dataset);
        eval.crossValidateModel(model, dataset, 10, new Random(1));

        return eval;
    }

    /**
     * Prints the results of the classification evaluation.
     *
     * @param eval hosts the classification results.
     */
    private static void printResults(final Evaluation eval) {
        System.out.println(eval.toSummaryString());
        System.out.printf("%-20s%.2f%n", "Accuracy", eval.pctCorrect());
        System.out.printf("%-20s%.2f%n", "Area Under ROC", eval.areaUnderROC(0));
        System.out.printf("%-20s%.2f%n", "F-Score", eval.fMeasure(0));
    }
}

