function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta

n = size(X,2); %number of features
hX = sigmoid(X * theta);

% y = 1 part
onePart = -y .* log(hX);
% y = 0 part
zeroPart =  (1-y) .* log(1 - hX);

% theta penalization part
thetaNfeatures = theta(2:end);
thetaPen = sum(thetaNfeatures .^2);
thetaPen = thetaPen * (lambda/(2*m));

%loss
J = sum(onePart - zeroPart) / m;
J = J + thetaPen;

% gradient
for count = 1:n
    if(count == 1)
        grad(count) = sum((hX - y) .* X(:,count)) / m;
    else
        penalize = (lambda/m) * thetaNfeatures;
        grad(count) = (sum((hX - y) .* X(:,count)) / m) + penalize(count-1);
    end;
end;


% =============================================================

end
