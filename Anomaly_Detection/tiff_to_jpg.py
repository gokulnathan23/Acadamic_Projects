import os
import shutil
import glob
from PIL import Image


def tif_to_jpg():
    i = 0
    for root, dirs, files in os.walk('data/UCSDped2'):
        for f in files:
            tif_im = Image.open(root+'/'+f)
            tif_im.save('dataset/training_data/image_'+str(i)+'.jpg')
            i += 1


def xml_img_mv():
    for xml_file in glob.glob('dataset/training_data/*.xml'):
        shutil.copy(xml_file, 'dataset/dt/')
        shutil.copy(xml_file[:-3]+'jpg', 'dataset/dt/')


# tif_to_jpg()
xml_img_mv()


