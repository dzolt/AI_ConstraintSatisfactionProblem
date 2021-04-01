#!/bin/python3
import json
import sys
import matplotlib.pyplot as plt
#import networkx as nx

class GraphDrawer:
    def __init__(self, data: dict):
        self.x_size = data['x_size']
        self.y_size = data['y_size']
        self.x_values = data['x_values']
        self.y_values = data['y_values']
        self.connected_points = data['connectedPoints']


    def get_image(self):
        plt.xlim(0, self.x_size + 1)
        plt.ylim(0, self.y_size + 1)
        plt.xticks([x for x in range(0, self.x_size + 2, 1)])
        plt.yticks([x for x in range(0, self.y_size + 2, 1)])
        plt.scatter(self.x_values, self.y_values)
        for i in range(0, len(self.connected_points)):
            for j in range(0, len(self.connected_points[i])):
                plt.plot([self.x_values[i], self.connected_points[i][j][0]], [self.y_values[i], self.connected_points[i][j][1]])
        #plt.plot(self.x_values, self.y_values)
        plt.grid()
        for i_x, i_y in zip(self.x_values, self.y_values):
           plt.text(int(i_x), int(i_y), '({}, {})'.format(i_x, i_y))


        return plt


def run_programm():
    def usage():
        print("usage: generate.py <input (*.json)> [<output> (*.png)]")
        sys.exit(1)
    argv = sys.argv[1:]
    output_path = ''
    if argv:
        if not(json_path := argv.pop(0)).lower().endswith('.json'):
            usage()
        if argv:
            output_path = argv.pop(0)
        if argv:
            usage()
    else:
        usage()

    with open(json_path, 'r') as json_file:
        drawer = GraphDrawer(json.load(json_file))
        image = drawer.get_image()

    if output_path:
        image.savefig(output_path)
    else:
        image.show()
    sys.exit(0)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    run_programm()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
