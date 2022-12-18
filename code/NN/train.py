import torch as tr
import pymongo as mongo
import numpy as np
import matplotlib.pyplot as plot

def getData():
    client = mongo.MongoClient("mongodb://localhost:27017/")
    db = client["xiang-qi"]
    tensor = db["tensor"]

    data = list(tensor.find({},{"_id": 0, "_class": 0}))
    print(data[0])
    boards = list(map(lambda e: e.get("board"), data))
    utilities = list(map(lambda e: e.get("score"), data))
    return (boards, utilities)

def encode(board):
    symbols = np.array([0,1,2,3,4,5,6,7,-1,-2,-3,-4,-5,-6,-7]).reshape(-1,1,1)
    onehot = (symbols == board).astype(np.float32)
    return tr.tensor(onehot)

class ConvNet(tr.nn.Module):
    def __init__(self, row, col, hid_features, kernel):
        super(ConvNet, self).__init__()
        self.to_hidden = tr.nn.Conv2d(15, hid_features, kernel)
        # self.to_output = tr.nn.Linear(hid_features*(row-kernel+1)*(col-kernel+1), 1)
        self.to_output = tr.nn.Linear(hid_features, 1)
    def forward(self, x):
        h = self.to_hidden(x)
        y = self.to_output(h.reshape(x.shape[0], -1))
        return y

def errors(net, batch):
    boards, utils = batch
    u = utils.reshape(-1, 1).float()
    y = net(boards)
    e = tr.sum((y - u)**2) / utils.shape[0]
    return e

if __name__ == "__main__":
    net = ConvNet(10, 9, 45, (10,9))
    optimizer = tr.optim.SGD(net.parameters(), lr=0.0001, momentum=0.9)

    boards, utils = getData()
    split_point = int(len(boards) * 0.7)

    training_boards = boards[:split_point]
    training_utils = utils[:split_point]
    trainning_batch = tr.stack(tuple(map(encode, training_boards))), tr.tensor(training_utils)

    testing_boards = boards[split_point:]
    testing_utils = utils[split_point:]
    testing_batch = tr.stack(tuple(map(encode, testing_boards))), tr.tensor(testing_utils)
    curves = [], []
    for i in range(2000):
        optimizer.zero_grad()

        e = errors(net, trainning_batch)
        e.backward()
        training_error = e.item()

        with tr.no_grad():
            e = errors(net, testing_batch)
            testing_error = e.item()
        optimizer.step()

        if i % 10 == 0:
            print("%d: %f, %f" % (i, training_error, testing_error))
        curves[0].append(training_error)
        curves[1].append(testing_error)

    tr.save(net.state_dict(), "./nn1")
    plot.plot(curves[0])
    plot.plot(curves[1])
    plot.xlabel("Iteration")
    plot.ylabel("Error")
    plot.title("Learning Curve")
    plot.legend(["Train", "Test"])
    plot.savefig("training.png")
    plot.show()
    