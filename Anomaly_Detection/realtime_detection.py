#usage python3 realtime_test.py trained_model/frozen_inference_graph.pb training_data/class_labels.pbtxt

import numpy as np
import sys
import tensorflow as tf
from distutils.version import StrictVersion
from object_detection.utils import label_map_util
from object_detection.utils import visualization_utils as vis_util
import cv2
import os

if StrictVersion(tf.__version__) < StrictVersion('1.9.0'):
    raise ImportError('Please upgrade your TensorFlow installation to v1.9.* or later!')

detection_graph = tf.Graph()
with detection_graph.as_default():
    od_graph_def = tf.GraphDef()
    with tf.gfile.GFile(sys.argv[1], 'rb') as fid:
        serialized_graph = fid.read()
        od_graph_def.ParseFromString(serialized_graph)
        tf.import_graph_def(od_graph_def, name='')

category_index = label_map_util.create_category_index_from_labelmap(sys.argv[2], use_display_name=True)


def load_image_into_numpy_array(image):
    (im_width, im_height) = image.size
    return np.array(image.getdata()).reshape((im_height, im_width, 3)).astype(np.uint8)


file_path = sys.argv[1]
folder_path = os.path.dirname(file_path)
cap = cv2.VideoCapture(0)
filename = folder_path+'/realtime_output.mp4'
codec = cv2.VideoWriter_fourcc('m', 'p', '4', 'v')
frame_rate = 30
resolution = (640, 480)

VideoFileOutput = cv2.VideoWriter(filename, codec, frame_rate, resolution)

with detection_graph.as_default():
    with tf.Session(graph=detection_graph) as sess:
        ret = True
        while ret:
            ret, image_np = cap.read()
            image_tensor = detection_graph.get_tensor_by_name('image_tensor:0')
            detection_boxes = detection_graph.get_tensor_by_name('detection_boxes:0')
            detection_scores = detection_graph.get_tensor_by_name('detection_scores:0')
            detection_classes = detection_graph.get_tensor_by_name('detection_classes:0')
            num_detections = detection_graph.get_tensor_by_name('num_detections:0')
            image_np_expanded = np.expand_dims(image_np, axis=0)
            (boxes, scores, classes, num) = sess.run([detection_boxes,
                                                      detection_scores,
                                                      detection_classes, num_detections],
                                                     feed_dict={image_tensor: image_np_expanded})
            vis_util.visualize_boxes_and_labels_on_image_array(
                image_np,
                np.squeeze(boxes),
                np.squeeze(classes).astype(np.int32),
                np.squeeze(scores),
                category_index,
                use_normalized_coordinates=True,
                line_thickness=8)

            VideoFileOutput.write(image_np)
            cv2.imshow('RealTime Detection', image_np)
            if cv2.waitKey(25) & 0xFF == ord('q'):
                break
                cv2.destroyAllWindows()
                cap.release()
