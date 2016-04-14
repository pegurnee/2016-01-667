function [ filehandle ] = ShowImage( filename )
%SHOWIMAGE Summary of this function goes here
%   Detailed explanation goes here
filehandle = imshow(uint8(load(filename)));

end

