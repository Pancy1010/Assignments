from flask import Flask, make_response
from pyecharts.charts import Bar
from pyecharts import options as opts
import pymysql

app = Flask(__name__)

x_list = []
y_list = []
with open("/home/jxh/Downloads/single_hour_count.csv", "r") as f:
    for i in f.readlines():
        strings = i.split(",")
        x = strings[0]
        y = int(strings[1])
        x_list.append(x)
        y_list.append(y)


@app.route('/')
def index():
    # Create the Pyecharts chart
    x_data = x_list
    y_data = y_list
    bar = (
        Bar()
        .add_xaxis(x_data)
        .add_yaxis('count', y_data)
        .set_global_opts(title_opts=opts.TitleOpts(title='count'),
                         tooltip_opts=opts.TooltipOpts(trigger="axis"),
                         datazoom_opts=[
                             opts.DataZoomOpts(
                                 range_start=0,
                                 range_end=50,
                                 is_zoom_lock=False
                             )
                         ]
                         )
        .render_embed()
    )

    # Embed the HTML code of the Pyecharts chart into the response object
    response = make_response(bar)
    response.headers['Content-Type'] = 'text/html'
    return response


if __name__ == '__main__':
    app.run()
