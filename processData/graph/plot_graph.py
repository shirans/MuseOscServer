import matplotlib.pyplot as plt


class PlotGraph:
    def __init__(self, input):
        self.input = input

    def plot(self):
        plt.plot(self.input.eeg_data.xs_time, self.input.eeg_data.alpha)
        plt.axis(input.eeg_data)
