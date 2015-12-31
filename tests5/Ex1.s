	.file	"Ex1.java"
	.section	.rodata
L0:	.string	"The result is \n\n"
	.align 8

	.align 8

	.section	.data

	.section	.text
	.align 8
	.globl main
	.type	main,@function
main:
	jmp	m_0
	.text
	.align 8
m_0:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L2:
	movq $3,%rdi
	movq %rdi, %rdi
	movq $4,%rsi
	addq %rsi, %rdi
	movq %rdi, %rdi
	movq $5,%rsi
	movq %rsi, %rdx
	movq $6,%rsi
	addq %rsi, %rdx
	imulq %rdx, %rdi
	callq b_intToString_0
	movq %rax, %rsi 
	leaq L0(%rip),%rdi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	callq b_printString_0
main_0:
	jmp L1
L1:
	leave
	ret


