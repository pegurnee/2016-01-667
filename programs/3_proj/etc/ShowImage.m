function ShowImage( filename )
%SHOWIMAGE Summary of this function goes here
%   Detailed explanation goes here
outname = strcat('restored_', filename(end), '.jpg');
imwrite(uint8(load(filename)), outname);

end

