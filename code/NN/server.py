from flask import Flask, request
import torch as tr
from train import ConvNet, encode
app = Flask(__name__)


net = ConvNet(10, 9, 45, (10, 9))
net.load_state_dict(tr.load("./nn"))
net.eval()
@app.route("/", methods=["POST"])
def home():
    content_type = request.headers.get('Content-Type')
    if (content_type == 'application/json'):
        states = request.json.get("states")
        utils = []
        for state in states:
            with tr.no_grad():
                utils.append(net(encode(state).unsqueeze(0))[0][0].item())
        res = {"utils": utils}
        return res


if __name__ == "__main__":
    app.run(host = "127.0.0.1", port=7070)