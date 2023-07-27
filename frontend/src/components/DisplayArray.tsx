import React from 'react';

const DisplayArray: React.FC<{ strings: string[] }> = ({ strings }) => {
  return (
    <div className="overflow-hidden space-y-2">
      {strings.map((str, index) => (
        <p key={index} className="whitespace-nowrap overflow-hidden">
          {str.slice(0,60)}
        </p>
      ))}
    </div>
  );
};

export default DisplayArray;
