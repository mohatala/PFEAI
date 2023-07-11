import pydicom
from pydicom.dataset import Dataset, FileDataset
from pydicom.uid import generate_uid
import numpy as np
from skimage import exposure
from skimage.feature import blob_log
import pydicom.data

import matplotlib.pyplot as plt

dcm = pydicom.dcmread('C:/Users/21260/Desktop/PFEAI/0002.dcm',force=True)
base = r"C:/Users/21260/Desktop/PFEAI/"
pass_dicom = "0002.dcm"  # file name is 1-12.dcm
filename = pydicom.data.data_manager.get_files(base,pass_dicom)[0]
  
ds = pydicom.dcmread(filename)
  
plt.imshow(ds.pixel_array, cmap=plt.cm.bone)  # set the color map to bone
plt.show()
