from PIL import Image
img = Image.open('C:/Users/21260/Desktop/PFEAI/gr1.jpg')

import numpy as np
img_gray = img.convert('L')
img_array = np.array(img_gray)
import pydicom
from pydicom.dataset import Dataset, FileDataset
from pydicom.uid import generate_uid

# Create a new dataset object
dataset = Dataset()

# Set the metadata
dataset.PatientName = "Test^Patient"
dataset.PatientID = "12345"
dataset.PatientBirthDate = "19800101"
dataset.Modality = "MR"
dataset.StudyDescription = "Test^Study"
dataset.Manufacturer = "Test^Manufacturer"
dataset.SeriesDescription = "Test^Series"
dataset.SeriesNumber = 1
dataset.StudyInstanceUID = generate_uid()
dataset.SeriesInstanceUID = generate_uid()
dataset.SOPInstanceUID = generate_uid()
dataset.Rows, dataset.Columns = img_array.shape
dataset.PixelData = img_array.tobytes()
dataset.file_meta = Dataset()  # Set the file meta information
dataset.is_little_endian = True
dataset.is_implicit_VR = True
pydicom.filewriter.write_file("output.dcm", dataset)
###############################################################
