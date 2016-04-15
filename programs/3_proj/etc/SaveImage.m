function SaveImage( filename )
%SAVEIMAGE Loads data to save as .jpg
%   Loads a file of pixel information and saves that information as
%    a '.jpg' image file. The output filename is dependant on the
%    input filename.
%
% @author eddie
outname = strcat('restored_', filename(end), '.jpg');
imwrite(uint8(load(filename)), outname);

end

